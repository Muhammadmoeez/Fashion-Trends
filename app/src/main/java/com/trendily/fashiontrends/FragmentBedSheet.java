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

public class FragmentBedSheet extends Fragment {

    View myBedSheetFragmentView;

    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase myFirebaseDatabase;
    DatabaseReference myDatabaseReference;
    private RecyclerView myRecyclerView;

    FirebaseRecyclerAdapter<BedSheetModel, MyBedSheetViewHolder>  adapter;

    public FragmentBedSheet() {

        myFirebaseDatabase = FirebaseDatabase.getInstance();
        myDatabaseReference = myFirebaseDatabase.getReference("BedSheet");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myBedSheetFragmentView = inflater.inflate(R.layout.bedsheet_fragment,container, false);

        myRecyclerView = (RecyclerView) myBedSheetFragmentView.findViewById(R.id.bedSheetRecyclerView);
        layoutManager = new LinearLayoutManager(getActivity());

        myRecyclerView.setLayoutManager(layoutManager);
        showPostlist();

        return myBedSheetFragmentView;
    }

    private void showPostlist() {

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<BedSheetModel>()
                .setQuery(myDatabaseReference, BedSheetModel.class).build();

        adapter = new FirebaseRecyclerAdapter<BedSheetModel, MyBedSheetViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyBedSheetViewHolder myBedSheetViewHolder, int i, @NonNull final BedSheetModel bedSheetModel) {



                myBedSheetViewHolder.shopName.setText(bedSheetModel.getShopName());
                Picasso.get().load(bedSheetModel.getImageURL()).into(myBedSheetViewHolder.postImage);
                myBedSheetViewHolder.postPrice.setText(bedSheetModel.getCategoryPrice());


                myBedSheetViewHolder.SubCategoryInterfaceClick(new CategoryOnClickShowFullPost() {
                    @Override
                    public void onClick(View view, boolean isLongPressed) {
                        Intent intent = new Intent(getContext(),ShowFullPost.class);

                        intent.putExtra("FullPostCode",bedSheetModel.getPostCode());
                        intent.putExtra("FullImageURL",bedSheetModel.getImageURL());
                        intent.putExtra("FullShopName",bedSheetModel.getShopName());
                        intent.putExtra("FullPostPrice",bedSheetModel.getCategoryPrice());
                        intent.putExtra("FullPostCity",bedSheetModel.getMemberCity());
                        intent.putExtra("FullPostPhone",bedSheetModel.getMemberPhone());
                        intent.putExtra("FullPostDescription",bedSheetModel.getCategoryDescription());

                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public MyBedSheetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clothe_design, parent,false);
                return new MyBedSheetViewHolder(view);
            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        myRecyclerView.setAdapter(adapter);
    }


    public static class MyBedSheetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView postCode;
        private ImageView postImage;
        private TextView postPrice;
        private TextView shopName;
        private TextView postCity;
        private TextView postPhone;
        private TextView postDescription;
        public CategoryOnClickShowFullPost categoryOnClickShowFullPost;

        public MyBedSheetViewHolder(@NonNull View itemView) {
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
