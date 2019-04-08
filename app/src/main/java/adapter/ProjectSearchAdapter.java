package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.brothersgas.R;

import java.util.ArrayList;

import model.ContractModel;
import model.OwnerModel;
import model.ProjectModel;

/**
 * Created by ashish.kumar on 08-04-2019.
 */

public class ProjectSearchAdapter extends ArrayAdapter {

    private ArrayList<OwnerModel> dataList;
    private Context mContext;
    private int itemLayout;

    private ListFilter listFilter = new ListFilter();
    private ArrayList<OwnerModel> dataListAllItems;



    public  ProjectSearchAdapter(Context context, int resource, ArrayList<OwnerModel> storeDataLst) {
        super(context, resource, storeDataLst);
        dataList = storeDataLst;
        mContext = context;
        itemLayout = resource;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public OwnerModel getItem(int position) {
        Log.d("CustomListAdapter",
                dataList.get(position).toString());
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.project_search_row, parent, false);
        }
        OwnerModel model=getItem(position);
        TextView project = (TextView) view.findViewById(R.id.project);
        TextView owner = (TextView) view.findViewById(R.id.owner);

        project.setText(model.getProjectName()+" - "+model.getProjectCode());
        owner.setText(model.getOwnerName()+" - "+model.getOwnerCode());

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<OwnerModel>(dataList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toUpperCase();

                ArrayList<OwnerModel> matchValues = new ArrayList<OwnerModel>();

                for (OwnerModel dataItem : dataListAllItems) {
                    if ((dataItem.getOwnerCode().toUpperCase().contains(searchStrLowerCase)) || (dataItem.getOwnerName().toUpperCase().contains(searchStrLowerCase)) || (dataItem.getProjectCode().toUpperCase().contains(searchStrLowerCase)) || (dataItem.getProjectName().toUpperCase().contains(searchStrLowerCase))) {
                        matchValues.add(dataItem);
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<OwnerModel>) results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }}