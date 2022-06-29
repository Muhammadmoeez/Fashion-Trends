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

public class FragmentClothes extends Fragment {

    View myClothesFragmentView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase myFirebaseDatabase;
    DatabaseReference myDatabaseReference;
    private RecyclerView myRecyclerView;


    FirebaseRecyclerAdapter<ClotheModel, MyClotheViewHolder> adapter;




    public FragmentClothes(){


        myFirebaseDatabase = FirebaseDatabase.getInstance();
        myDatabaseReference = myFirebaseDatabase.getReference("Clothes");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myClothesFragmentView = inflater.inflate(R.layout.clothes_fragment, container,false);
        myRecyclerView = (RecyclerView) myClothesFragmentView.findViewById(R.id.clotheRecyclerView);
        layoutManager = new LinearLayoutManager(getActivity());

        myRecyclerView.setLayoutManager(layoutManager);

        showPostlist();

        return myClothesFragmentView;
    }

    private void showPostlist() {


        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ClotheModel>()
                .setQuery(myDatabaseReference, ClotheModel.class).build();

        adapter = new FirebaseRecyclerAdapter<ClotheModel, MyClotheViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyClotheViewHolder myClotheViewHolder, int i, @NonNull final ClotheModel clotheModel) {


        //        myClotheViewHolder.postCode.setText(clotheModel.getPostCode());

                myClotheViewHolder.shopName.setText(clotheModel.getShopName());
                Picasso.get().load(clotheModel.getImageURL()).into(myClotheViewHolder.postImage);
                myClotheViewHolder.postPrice.setText(clotheModel.getCategoryPrice());



//                myClotheViewHolder.postCity.setText(clotheModel.getMemberCity());
//                myClotheViewHolder.postPhone.setText(clotheModel.getMemberPhone());
//                myClotheViewHolder.postDescription.setText(clotheModel.getCategoryDescription());




                myClotheViewHolder.SubCategoryInterfaceClick(new CategoryOnClickShowFullPost() {
                    @Override
                    public void onClick(View view, boolean isLongPressed) {


                        Intent intent = new Intent(getContext(),ShowFullPost.class);


                        intent.putExtra("FullPostCode",clotheModel.getPostCode());
                        intent.putExtra("FullImageURL",clotheModel.getImageURL());
                        intent.putExtra("FullShopName",clotheModel.getShopName());
                        intent.putExtra("FullPostPrice",clotheModel.getCategoryPrice());
                        intent.putExtra("FullPostPhone",clotheModel.getMemberPhone());
                        intent.putExtra("FullPostCity",clotheModel.getMemberCity());
                        intent.putExtra("FullPostDescription",clotheModel.getCategoryDescription());



                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public MyClotheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clothe_design, parent,false);

                return new MyClotheViewHolder(view);
            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        myRecyclerView.setAdapter(adapter);
    }


    public static class MyClotheViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener  {



        private TextView postCode;
        private ImageView postImage;
        private TextView postPrice;
        private TextView shopName;
        private TextView postCity;
        private TextView postPhone;
        private TextView postDescription;

        public CategoryOnClickShowFullPost categoryOnClickShowFullPost;

        public MyClotheViewHolder(@NonNull View itemView) {
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



