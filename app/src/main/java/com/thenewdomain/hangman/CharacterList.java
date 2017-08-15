package com.thenewdomain.hangman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class CharacterList extends AppCompatActivity {

    int rankPoints;

    int[] IMAGES = {R.drawable.character_billy,
            R.drawable.character_nagase,
            R.drawable.character_leokim,
            R.drawable.character_drjames,
            R.drawable.character_yasuo,
            R.drawable.character_carter};
    String[] NAMES = {"Billy",
            "Nagase",
            "Leo Kim",
            "Dr. James",
            "Yasuo",
            "Carter"};
    String[] DESCRIPTIONS = {"A detective wannabe.\nOften mistaken as a stalker.\nAdmires Detective Carter.",
            "Likes to play sudoku.\nTrains under his father to become a true samurai.",
            "Left Harvard to pursue his dream as a K-Pop singer.\nEyebrows always on fleek.",
            "A Mathematics professor by day, a gamer by night.\nUnbeatable in StarCraft.",
            "Nagase's father.\nNot very impressed by his son's prowess as a samurai.",
            "Has a particular set of skills.\nSkills he has acquired over a very long career as a detective."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);

        ListView lvCharacter = (ListView) findViewById(R.id.lvCharacter);
        CustomAdapter customAdapter = new CustomAdapter();
        lvCharacter.setAdapter(customAdapter);

        Button btnBackFromCharacterList = (Button) findViewById(R.id.btnBackFromCharacterList);
        btnBackFromCharacterList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CharacterList.this, MainMenu.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    class CustomAdapter extends BaseAdapter{

        final private String TAG = "CustomAdapter";

        @Override
        public int getCount() {
            return IMAGES.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.one_character, null);
            ImageView ivCharacterImage = (ImageView) convertView.findViewById(R.id.ivCharacterImage);
            final TextView tvCharacterName = (TextView) convertView.findViewById(R.id.tvCharacterName);
            TextView tvAbilityDescription = (TextView) convertView.findViewById(R.id.tvAbilityDescription);
            final Button btnUseCharacter = (Button) convertView.findViewById(R.id.btnUseCharacter);

            ivCharacterImage.setImageResource(IMAGES[position]);
            tvCharacterName.setText(NAMES[position]);
            tvAbilityDescription.setText(DESCRIPTIONS[position]);

            if(notUnlockedYet(tvCharacterName)){
                btnUseCharacter.setText("Locked");
                btnUseCharacter.setEnabled(false);
                ivCharacterImage.setImageResource(R.drawable.unknowncharacter);

                String tempText = "Collect ";

                switch(tvCharacterName.getText().toString().trim()){
                    case "Nagase":
                        tempText += (1000 - rankPoints);
                        break;
                    case "Leo Kim":
                        tempText += (2000 - rankPoints);
                        break;
                    case "Dr. James":
                        tempText += (3500 - rankPoints);
                        break;
                    case "Yasuo":
                        tempText += (5000 - rankPoints);
                        break;
                    case "Carter":
                        tempText += (7000 - rankPoints);
                        break;
                }

                tempText += " more rank points to unlock this brawler.";
                tvAbilityDescription.setText(tempText);
            }

            btnUseCharacter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toCharacterAbility = new Intent (CharacterList.this, CharacterAbility.class);
                    toCharacterAbility.putExtra("NAME", tvCharacterName.getText().toString().trim());
                    startActivity(toCharacterAbility);
                }
            });

            return convertView;
        }
    }

    private boolean notUnlockedYet(TextView tvCharacterName) {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rankPoints = dataSnapshot.child("rankPoints").getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        switch(tvCharacterName.getText().toString().trim()){
            case "Billy":
                if(rankPoints >= 0) return false;
                break;
            case "Nagase":
                if(rankPoints >= 1000) return false;
                break;
            case "Leo Kim":
                if(rankPoints >= 2000) return false;
                break;
            case "Dr. James":
                if(rankPoints >= 3500) return false;
                break;
            case "Yasuo":
                if(rankPoints >= 5000) return false;
                break;
            case "Carter":
                if(rankPoints >= 7000) return false;
                break;
        }

        return true;
    }
}
