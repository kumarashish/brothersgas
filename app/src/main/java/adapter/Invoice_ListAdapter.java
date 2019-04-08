package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.brothersgas.R;

import java.util.ArrayList;

import interfaces.InvoiceListItemClickListner;
import interfaces.ListItemClickListner;
import model.ContractModel;
import model.InvoiceModel;
import utils.Utils;

public class Invoice_ListAdapter  extends BaseAdapter {
    ArrayList<InvoiceModel> list;
    Activity act;
    LayoutInflater inflater;
    InvoiceListItemClickListner callback;

    public Invoice_ListAdapter(ArrayList<InvoiceModel> list, Activity act) {
        this.list = list;
        this.act = act;
        inflater = act.getLayoutInflater();
        callback=(InvoiceListItemClickListner)act;
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
        final InvoiceModel model = list.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.invoice_row, null, true);
            holder.invoice_num = (TextView) convertView.findViewById(R.id.invoice_number);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.tot_amount = (TextView) convertView.findViewById(R.id.tot_amount);
            holder.out_amount = (TextView) convertView.findViewById(R.id.out_amount);
            holder.customerName= (TextView) convertView.findViewById(R.id.customer);
            holder.detailsView = (View) convertView.findViewById(R.id.detailsView);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.invoice_num.setText(model.getSales_Invoice_Number());
        holder.date.setText(Utils.getNewDate(model.getDate() ) );
        holder.tot_amount.setText(model.getAmount()+" "+model.getTotal_amount_currency());
        holder.out_amount.setText(model.getOutstanding_amount()+" "+model.getOutstanding_amount_currency());
        holder.customerName.setText(model.getCustomer_Description()+"("+model.getCustomer()+")");

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
        TextView invoice_num;
        TextView date, tot_amount, out_amount,customerName;
        View detailsView;
    }
}