package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.brothersgas.R;

import java.util.ArrayList;

import interfaces.InvoiceItemsForPrint;
import interfaces.ListItemClickListner;
import model.ContractModel;
import model.InvoiceForPrintModel;
import utils.Utils;

/**
 * Created by ashish.kumar on 04-04-2019.
 */

public class InvoiceListItemAdapter extends BaseAdapter {
    ArrayList<InvoiceForPrintModel> list;
    Activity act;
    LayoutInflater inflater;
    InvoiceItemsForPrint callback;

    public InvoiceListItemAdapter(ArrayList<InvoiceForPrintModel> list, Activity act) {
        this.list = list;
        this.act = act;
        inflater = act.getLayoutInflater();
        callback = (InvoiceItemsForPrint) act;
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
        final InvoiceForPrintModel model = list.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.print_row, null, true);
            holder. doc_num = (TextView) convertView.findViewById(R.id.doc_num);
            holder.customer_name = (TextView) convertView.findViewById(R.id.customer_name);
            holder.amount = (TextView) convertView.findViewById(R.id.amount);

            holder.date = (TextView) convertView.findViewById(R.id.date);

            holder.detailsView = (View) convertView.findViewById(R.id.detailsView);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.doc_num.setText(model.getInvoiceNumber());
        holder.customer_name.setText(model.getCustomer());
        holder.amount .setText(model.getCurrency() +" "+model.getAmount());
        holder.date.setText(Utils.getNewDate(model.getInvoiceDate()));
        holder.detailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(model);

            }
        });
        convertView.setTag(holder);
        return convertView;
    }

    public class ViewHolder {
        TextView amount;
        TextView doc_num;
        TextView customer_name;
        TextView date;
        View detailsView;
    }
}