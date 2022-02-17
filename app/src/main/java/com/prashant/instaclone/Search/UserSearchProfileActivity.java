package com.prashant.instaclone.Search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.prashant.instaclone.FollowersFollowing;
import com.prashant.instaclone.R;
import com.prashant.instaclone.Utils.GridImageAdapter;
import com.prashant.instaclone.models.Comments;
import com.prashant.instaclone.models.Likes;
import com.prashant.instaclone.models.Photo;
import com.prashant.instaclone.models.Users;

public class UserSearchProfileActivity extends AppCompatActivity {

    private static final String TAG ="UserSearchActivity" ;
    private static final int NUM_GRID_COLUMNS = 3;

    String searchedUserId;
    Button Follow,Following,Message;
    ImageView profilePhoto;
    GridView gridView;
    TextView posts,followers,followings,name, description,website,username;
    LinearLayout follower,following;
    String noFollowers,noFollowings;
    DatabaseReference databaseReference;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_search_profile);

        searchedUserId = getIntent().getStringExtra("SearchedUserid");
        Log.d(TAG, "Item Clicked Getting UID "+searchedUserId);

        Follow = findViewById(R.id.UserSearchProfile_Followbtn);
        Following = findViewById(R.id.UserSearchProfile_Followingbtn);
        Message = findViewById(R.id.UserSearchProfile_messages);
        profilePhoto = findViewById(R.id.UserSearchProfile_user_img);
        gridView = findViewById(R.id.UserSearchProfile_gridview1);
        posts = findViewById(R.id.UserSearchProfile_txtPosts);
        followers = findViewById(R.id.UserSearchProfile_txtFollowers);
        followings = findViewById(R.id.UserSearchProfile_txtFollowing);
        name = findViewById(R.id.UserSearchProfile_display_name);
        description = findViewById(R.id.UserSearchProfile_description);
        website = findViewById(R.id.UserSearchProfile_website);
        username = findViewById(R.id.UserSearchProfile_profileName);
        follower = findViewById(R.id.UserSearchProfile_noFollowers);
        following = findViewById(R.id.UserSearchProfile_noFollowing);

        mProgressBar = findViewById(R.id.UserSearchProfile_ProgressBar);

        RetrivingGeneralData();

        Follow.setOnClickListener(v -> {

            Log.d(TAG, "onClick: Follow: " + searchedUserId);

            FirebaseDatabase.getInstance().getReference()
                    .child("Following")
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                    .child(searchedUserId)
                    .child("user_id")
                    .setValue(searchedUserId);

            FirebaseDatabase.getInstance().getReference()
                    .child("Followers")
                    .child(searchedUserId)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("user_id")
                    .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
            setFollowing();
            increaseFollowers();
            increaseFollowing();
            addFollowNotification(searchedUserId);

        });
        Following.setOnClickListener(v -> {

            Log.d(TAG, "onClick: Following: " + searchedUserId);

            FirebaseDatabase.getInstance().getReference()
                    .child("Following")
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                    .child(searchedUserId)
                    .removeValue();

            FirebaseDatabase.getInstance().getReference()
                    .child("Followers")
                    .child(searchedUserId)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .removeValue();
            setUnfollowing();
            decreaseFollowers();
            decreaseFollowing();

        });



    }

    private void RetrivingGeneralData(){

        if(searchedUserId.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())){
            // Current User

            Follow.setVisibility(View.GONE);
            Message.setVisibility(View.GONE);
            Following.setVisibility(View.GONE);
            dataretrive();
            tempGridSetup();


        }else{
//             Other User
            dataretrive();
            tempGridSetup();
            isFollowing();




        }
        dataretrive();
        tempGridSetup();

    }

    private void dataretrive(){

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(searchedUserId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final Users user = snapshot.getValue(Users.class);
                posts.setText(Objects.requireNonNull(user).getPosts());
                noFollowers = user.getFollowers();
                noFollowings = user.getFollowing();
                followers.setText(noFollowers);
                followings.setText(noFollowings);
                name.setText(user.getFullName());
                description.setText(user.getDiscription());
                website.setText(user.getWebsite());
                username.setText(user.getUsername());
                Glide.with(UserSearchProfileActivity.this)
                        .load(user.getProfilePhoto())
                        .into(profilePhoto);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void tempGridSetup(){
        Log.d(TAG, "setupGridView: Setting up image grid.");

        follower.setOnClickListener(v -> {

            Intent intent = new Intent(UserSearchProfileActivity.this, FollowersFollowing.class);
            intent.putExtra("id",searchedUserId);
            intent.putExtra("title","Followers");
            intent.putExtra("number",noFollowers);
            startActivity(intent);
            overridePendingTransition(0,0);

        });

        following.setOnClickListener(v -> {
            Intent intent = new Intent(UserSearchProfileActivity.this,FollowersFollowing.class);
            intent.putExtra("id",searchedUserId);
            intent.putExtra("title","Following");
            intent.putExtra("number",noFollowings);
            startActivity(intent);
            overridePendingTransition(0,0);

        });


        final ArrayList<Photo> photos = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("User_Photo")
                .child(searchedUserId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for ( DataSnapshot singleSnapshot :  snapshot.getChildren()){
                    Photo photo = new Photo();
                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                    photo.setCaption(Objects.requireNonNull(Objects.requireNonNull(objectMap).get("caption")).toString());
                    photo.setTags(Objects.requireNonNull(objectMap.get("tags")).toString());
                    photo.setPhoto_id(Objects.requireNonNull(objectMap.get("photo_id")).toString());
                    photo.setUser_id(Objects.requireNonNull(objectMap.get("user_id")).toString());
                    photo.setDate_Created(Objects.requireNonNull(objectMap.get("date_Created")).toString());
                    photo.setImage_Path(Objects.requireNonNull(objectMap.get("image_Path")).toString());

                    List<Comments> comments = new ArrayList<>();
                    for (DataSnapshot dSnapshot : singleSnapshot
                            .child("comments").getChildren()){
                        Comments comment = new Comments();
                        comment.setUser_id(Objects.requireNonNull(dSnapshot.getValue(Comments.class)).getUser_id());
                        comment.setComment(Objects.requireNonNull(dSnapshot.getValue(Comments.class)).getComment());
                        comment.setDate_created(Objects.requireNonNull(dSnapshot.getValue(Comments.class)).getDate_created());
                        comments.add(comment);
                    }

                    photo.setComments(comments);

                    List<Likes> likesList = new ArrayList<>();
                    for (DataSnapshot dSnapshot : singleSnapshot
                            .child("likes").getChildren()){
                        Likes like = new Likes();
                        like.setUser_id(Objects.requireNonNull(dSnapshot.getValue(Likes.class)).getUser_id());
                        likesList.add(like);
                    }
                    photo.setLikes(likesList);
                    photos.add(photo);
                }
//                setup our image grid
                int gridWidth = getResources().getDisplayMetrics().widthPixels;
                int imageWidth = gridWidth/NUM_GRID_COLUMNS;
                gridView.setColumnWidth(imageWidth);

                ArrayList<String> imgUrls = new ArrayList<>();
                for(int i = 0; i < photos.size(); i++){
                    imgUrls.add(photos.get(i).getImage_Path());
                }
                GridImageAdapter adapter = new GridImageAdapter(UserSearchProfileActivity.this,R.layout.layout_grid_imageview,
                        "", imgUrls);
                gridView.setAdapter(adapter);

                gridView.setOnItemClickListener((parent, view, position, id) -> {

                    Log.d(TAG, "Item Clicked Getting Bundle "+photos.get(position));
                    Intent intent=new Intent(UserSearchProfileActivity.this, UserSearchViewPost.class);
                    intent.putExtra("SearchedUserPhoto",photos.get(position));
                    intent.putExtra("Commentcount",photos.get(position).getComments().size());

                    startActivity(intent);
                    overridePendingTransition(0,0);

                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: query cancelled.");

            }
        });
    }

    private void setFollowing(){
        Log.d(TAG, "setFollowing: updating UI for following this user");
        Follow.setVisibility(View.GONE);
        Following.setVisibility(View.VISIBLE);
    }

    private void setUnfollowing(){
        Log.d(TAG, "setFollowing: updating UI for unfollowing this user");
        Follow.setVisibility(View.VISIBLE);
        Following.setVisibility(View.GONE);
    }

    private void isFollowing(){
        Log.d(TAG, "isFollowing: checking if following this users.");
        setUnfollowing();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Following")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .orderByChild("user_id").equalTo(searchedUserId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found user:" + singleSnapshot.getValue());

                    setFollowing();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void increaseFollowing(){
        Log.d(TAG, "increaseFollowing: Increasing Following Count");

        final DatabaseReference data = FirebaseDatabase.getInstance().getReference("Users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String postCount = Integer.toString(Integer.parseInt(Objects.requireNonNull(snapshot.child("following").getValue()).toString()) + 1);
                data.child("following").setValue(postCount);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    public void decreaseFollowing(){
        Log.d(TAG, "decreaseFollowing: decreasing Following Count");

        final DatabaseReference data = FirebaseDatabase.getInstance().getReference("Users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String postCount = Integer.toString(Integer.parseInt(Objects.requireNonNull(snapshot.child("following").getValue()).toString()) - 1);
                data.child("following").setValue(postCount);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    public void increaseFollowers(){
        Log.d(TAG, "increaseFollowers: increasing Followers Count");

        final DatabaseReference data = FirebaseDatabase.getInstance().getReference("Users")
                .child(searchedUserId);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String postCount = Integer.toString(Integer.parseInt(Objects.requireNonNull(snapshot.child("followers").getValue()).toString()) + 1);
                data.child("followers").setValue(postCount);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    public void decreaseFollowers(){
        Log.d(TAG, "decreaseFollowers: decreasing Followers Count");

        final DatabaseReference data = FirebaseDatabase.getInstance().getReference("Users")
                .child(searchedUserId);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String postCount = Integer.toString(Integer.parseInt(Objects.requireNonNull(snapshot.child("followers").getValue()).toString()) - 1);
                data.child("followers").setValue(postCount);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void addFollowNotification(String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications");

        HashMap<String, Object> hashMappp = new HashMap<>();
        hashMappp.put("userid", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        hashMappp.put("text", "started following you");
        hashMappp.put("postid", "");
        hashMappp.put("ispost", false);
        reference.child(userid).push().setValue(hashMappp);

    }


}