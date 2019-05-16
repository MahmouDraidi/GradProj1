package com.gradproj1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AutocompleteArrayAdapter extends ArrayAdapter {
    private ArrayList<String> linesFull;

    public AutocompleteArrayAdapter(@NonNull Context context, @NonNull List linesList) {
        super(context, 0, linesList);
        linesFull = new ArrayList<>(linesList);
    }

    @Override
    public Filter getFilter() {
        return linesFilter;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_line_search_row, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.text_view_list_item);

        String s = (String) getItem(position);

        if (s != null) {
            textViewName.setText(s);
        }

        return convertView;
    }

    private Filter linesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            List<String> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(linesFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (String item : linesFull) {
                    if (item.toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            clear();
            addAll((List) filterResults.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((String) resultValue);

        }
    };
}
