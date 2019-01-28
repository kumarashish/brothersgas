package adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.brothersgas.R;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

import interfaces.ListItemClickListner;
import model.ContractModel;

/**
 * Created by ashish.kumar on 25-01-2019.
 */

public class ContractListAdapter extends BaseAdapter {
    ArrayList<ContractModel> list;
    Activity act;
    LayoutInflater inflater;
    ListItemClickListner callback;

    public ContractListAdapter(ArrayList<ContractModel> list, Activity act)
    {
        this.list=list;
        this.act=act;
        inflater = act.getLayoutInflater();
        callback=(ListItemClickListner)act;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ContractModel model = list.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.contract_row, null, true);
            holder.meter_number=(TextView)convertView.findViewById(R.id.meter_number);
            holder.customer_name=(TextView)convertView.findViewById(R.id.customer_name);
            holder.address=(TextView)convertView.findViewById(R.id.address);
            holder.date=(TextView)convertView.findViewById(R.id.date);
            holder.cancel=(Button)convertView.findViewById(R.id.cancel);
            holder.block=(Button)convertView.findViewById(R.id.block);
            holder.detailsView=(View)convertView.findViewById(R.id.detailsView);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.meter_number.setText(model.getContract_Meternumber());
        holder.customer_name.setText(model.getCustomername());
        holder.address.setText(model.getPlaceName() + "," + model.getAddresscode());
        holder.date.setText(model.getContactcreationdate());
        holder.detailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(model);

            }
        });
        holder.block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onBlockClick(model);

            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onCancelClick(model);
            }
        });
        convertView.setTag(holder);
        return convertView;
    }

    public class ViewHolder{
       TextView meter_number;
        TextView customer_name;
        TextView  address;
        TextView date;
        Button cancel;
        Button block;
        View detailsView;
    }
}