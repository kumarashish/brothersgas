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

public class CustomListAdapter extends ArrayAdapter {

    private ArrayList<ContractModel> dataList;
    private Context mContext;
    private int itemLayout;

    private ListFilter listFilter = new ListFilter();
    private ArrayList<ContractModel> dataListAllItems;



    public CustomListAdapter(Context context, int resource, ArrayList<ContractModel> storeDataLst) {
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
    public ContractModel getItem(int position) {
        Log.d("CustomListAdapter",
                dataList.get(position).toString());
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.contract_row, parent, false);
        }
ContractModel model=getItem(position);
        TextView strName = (TextView) view.findViewById(R.id.customer_name);
        TextView strNumber = (TextView) view.findViewById(R.id.meter_number);
        strName.setText(model.getCustomername()+" ("+model.getCustomercode()+")");
        strNumber.setText(model.getContract_Meternumber());
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
                    dataListAllItems = new ArrayList<ContractModel>(dataList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toUpperCase();

                ArrayList<ContractModel> matchValues = new ArrayList<ContractModel>();

                for (ContractModel dataItem : dataListAllItems) {
                    if( (dataItem.getContract_Meternumber().toUpperCase().contains(searchStrLowerCase))||(dataItem.getCustomername().toUpperCase().contains(searchStrLowerCase))) {
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
                dataList = (ArrayList<ContractModel>)results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
