package com.zybooks.thebanddatabase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ListFragment extends Fragment {

    //list fragment allows scrolling through bands

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        //Click listener for recycleViewer
        View.OnClickListener onClickListener = itemView -> {
            //create frag args contained selected band ID
            int selectedBandId = (int) itemView.getTag();
            Bundle args = new Bundle();
            args.putInt(DetailFragment.ARG_BAND_ID, selectedBandId);

            View detailFragmentContainer = rootView.findViewById(R.id.detail_frag_container);
            if(detailFragmentContainer == null) {
                //replace list with details
                Navigation.findNavController(itemView).navigate(R.id.show_item_detail, args);
            }
            else{
                //show details on the right
                Navigation.findNavController(detailFragmentContainer).navigate(R.id.fragment_detail, args);
            }

        };

        // Send bands to RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.band_list);
        List<Band> bands = BandRepository.getInstance(requireContext()).getBands();
        recyclerView.setAdapter(new BandAdapter(bands, onClickListener));

        return rootView;
    }

    private class BandAdapter extends RecyclerView.Adapter<BandHolder> {

        private final List<Band> mBands;
        private final View.OnClickListener mOnClickListener;

        public BandAdapter(List<Band> bands, View.OnClickListener onClickListener) {
            mBands = bands;
            mOnClickListener = onClickListener;
        }

        @NonNull
        @Override
        public BandHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new BandHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(BandHolder holder, int position) {
            Band band = mBands.get(position);
            holder.bind(band);
            holder.itemView.setTag(band.getId());
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mBands.size();
        }
    }

    private static class BandHolder extends RecyclerView.ViewHolder {

        private final TextView mNameTextView;

        public BandHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_band, parent, false));
            mNameTextView = itemView.findViewById(R.id.band_name);
        }

        public void bind(Band band) {
            mNameTextView.setText(band.getName());
        }
    }
}