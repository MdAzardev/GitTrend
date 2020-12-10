package andr.com.gittrend;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<ExampleItem> mExampleList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mimage;
        public TextView mname;
        public TextView mlanguages;
        public TextView mdescp;
        public  TextView mstars;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mname = (TextView) itemView.findViewById(R.id.item_title);
            mlanguages = (TextView) itemView.findViewById(R.id.item_likes);
            mdescp = (TextView) itemView.findViewById(R.id.item_desc);
            mstars = (TextView) itemView.findViewById(R.id.item_likes2);
            mimage = (ImageView) itemView.findViewById(R.id.item_profile_img);

        }
    }

    public ExampleAdapter(ArrayList<ExampleItem> exampleList) {
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout,
                parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);
        Picasso.get().load(currentItem.getImageResource()).into(holder.mimage);
        holder.mname.setText(currentItem.getText1());
        holder.mlanguages.setText(currentItem.getText2());
        holder.mdescp.setText(currentItem.getText3());
        holder.mstars.setText(currentItem.getText4());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public void filterList(ArrayList<ExampleItem> filteredList) {
        mExampleList = filteredList;
        notifyDataSetChanged();
    }
}
