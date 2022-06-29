package com.trendily.fashiontrends;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FragmentCurtain extends Fragment {

    View myCurtainFragmentView;

    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase myFirebaseDatabase;
    DatabaseReference myDatabaseReference;
    private RecyclerView myRecyclerView;


    FirebaseRecyclerAdapter<CurtainModel, MyCurtainViewHolder> adapter;

    public FragmentCurtain(){


        myFirebaseDatabase = FirebaseDatabase.getInstance();
        myDatabaseReference = myFirebaseDatabase.getReference("Curtain");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myCurtainFragmentView = inflater.inflate(R.layout.curtain_fragment, container,false);

        myRecyclerView = (RecyclerView) myCurtainFragmentView.findViewById(R.id.curtainRecyclerView);
        layoutManager = new LinearLayoutManager(getActivity());

        myRecyclerView.setLayoutManager(layoutManager);

        showPostlist();

        return myCurtainFragmentView;
    }

    private void showPostlist() {

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<CurtainModel>()
                .setQuery(myDatabaseReference,CurtainModel.class).build();

        adapter = new FirebaseRecyclerAdapter<CurtainModel, MyCurtainViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyCurtainViewHolder myCurtainViewHolder, int i, @NonNull final CurtainModel curtainModel) {

                myCurtainViewHolder.shopName.setText(curtainModel.getShopName());
              //  myCurtainViewHolder.postCode.setText(curtainModel.getPostCode());
                Picasso.get().load(curtainModel.getImageURL()).into(myCurtainViewHolder.postImage);
                myCurtainViewHolder.postPrice.setText(curtainModel.getCategoryPrice());
//                myCurtainViewHolder.postCity.setText(curtainModel.getMemberCity());
//                myCurtainViewHolder.postPhone.setText(curtainModel.getMemberPhone());
//                myCurtainViewHolder.postDescription.setText(curtainModel.getCategoryDescription());


                myCurtainViewHolder.SubCategoryInterfaceClick(new CategoryOnClickShowFullPost() {
                    @Override
                    public void onClick(View view, boolean isLongPressed) {
                        Intent intent = new Intent(getContext(),ShowFullPost.class);


                        intent.putExtra("FullPostCode",curtainModel.getPostCode());
                        intent.putExtra("FullImageURL",curtainModel.getImageURL());
                        intent.putExtra("FullShopName",curtainModel.getShopName());
                        intent.putExtra("FullPostPrice",curtainModel.getCategoryPrice());
                        intent.putExtra("FullPostCity",curtainModel.getMemberCity());
                        intent.putExtra("FullPostPhone",curtainModel.getMemberPhone());
                        intent.putExtra("FullPostDescription",curtainModel.getCategoryDescription());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public MyCurtainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clothe_design, parent,false);
                return new MyCurtainViewHolder(view);
            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        myRecyclerView.setAdapter(adapter);
    }

    public static class MyCurtainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        private TextView postCode;
        private ImageView postImage;
        private TextView postPrice;
        private TextView shopName;
        private TextView postCity;
        private TextView postPhone;
        private TextView postDescription;

        public CategoryOnClickShowFullPost categoryOnClickShowFullPost;

        public MyCurtainViewHolder (@NonNull View itemView) {
            super(itemView);

        //    postCode = (TextView) itemView.findViewById(R.id.rPostCode);
            postImage = (ImageView) itemView.findViewById(R.id.rImageView);
            shopName = (TextView) itemView.findViewById(R.id.rshopname);
            postPrice = (TextView) itemView.findViewById(R.id.rPrice);
//            postCity = (TextView) itemView.findViewById(R.id.rCity);
//            postPhone = (TextView) itemView.findViewById(R.id.rPhone);
//            postDescription = (TextView) itemView.findViewById(R.id.rDescription);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            categoryOnClickShowFullPost.onClick(view,false);


        }


        public void SubCategoryInterfaceClick(CategoryOnClickShowFullPost myCategoryOnClickShowFullPost){

            this.categoryOnClickShowFullPost = myCategoryOnClickShowFullPost;

        }
    }
}
