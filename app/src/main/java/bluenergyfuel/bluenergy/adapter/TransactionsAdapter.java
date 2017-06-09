package bluenergyfuel.bluenergy.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bluenergyfuel.bluenergy.R;
import bluenergyfuel.bluenergy.model.TransactionList;

/**
 * Created by jockinjc0 on 4/16/17.
 */


public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.PersonViewHolder> {
    private List<TransactionList> transactionLists;
    private static final String branch = " Branch";

    public TransactionsAdapter(List<TransactionList> transactionLists){
        this.transactionLists = transactionLists;
    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.drawer_transactions_list, viewGroup, false);
        return new PersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {

        if (i % 2 == 0){
            personViewHolder.textDate.setText(transactionLists.get(i).getDate());
            personViewHolder.textTime.setText(transactionLists.get(i).getTime());
            personViewHolder.textAmount.setText(transactionLists.get(i).getAmount());
            personViewHolder.textAmount.setTextColor(Color.parseColor("#0084b4"));
        }
        personViewHolder.textStation.setText(transactionLists.get(i).getStation()+branch);
        personViewHolder.textDate.setText(transactionLists.get(i).getDate());
        personViewHolder.textTime.setText(transactionLists.get(i).getTime());
        personViewHolder.textAmount.setText(transactionLists.get(i).getAmount());
    }

    @Override
    public int getItemCount() {
        if (transactionLists != null) {
            return transactionLists.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView textDate;
        TextView textTime;
        TextView textAmount;
        TextView textStation;
        ImageView imageView;

        PersonViewHolder(View itemView) {
            super(itemView);
            textDate = (TextView) itemView.findViewById(R.id.text_date);
            textTime = (TextView) itemView.findViewById(R.id.text_time);
            textAmount = (TextView) itemView.findViewById(R.id.text_amount);
            textStation = (TextView) itemView.findViewById(R.id.text_station);
            imageView = (ImageView) itemView.findViewById(R.id.peso_logo);
        }
    }

}
