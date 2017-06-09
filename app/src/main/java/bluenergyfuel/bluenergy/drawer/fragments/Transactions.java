package bluenergyfuel.bluenergy.drawer.fragments;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import bluenergyfuel.bluenergy.R;
import bluenergyfuel.bluenergy.adapter.TransactionsAdapter;
import bluenergyfuel.bluenergy.extendables.BaseFragment;
import bluenergyfuel.bluenergy.model.TransactionList;
public class Transactions extends BaseFragment {
    private static String stringDate, stringTime;
    //Array List for transaction List
    private List<TransactionList> tl = new ArrayList<>();
    //adapter
    final TransactionsAdapter transactionsAdapter = new TransactionsAdapter(tl);

    public static final String ARGS_JSON_OBJ = "json";
    public Transactions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.drawer_transactions, container, false);
        String myJson = getArguments().getString(ARGS_JSON_OBJ);
        dataProcessing(myJson);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.transactions_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(transactionsAdapter);
        return view;
    }
    public void dataProcessing(String jsonString){
        String oiltime, amountTemp;
        try {
            tl.clear();
            JSONObject response = new JSONObject(jsonString);
            JSONArray array = response.getJSONArray("RecentTransaction");
            for (int i = 0; i < array.length(); i++){
                TransactionList transactionList = new TransactionList();
                JSONObject fetchdata = array.getJSONObject(i);
                //
                amountTemp = fetchdata.getString("TradeMoney");
                oiltime = fetchdata.getString("OilTime");
                //
                transactionList.setStation(fetchdata.getString("StationName"));
                transactionList.setAmount(amountTemp.substring(0,amountTemp.length() - 1));
                transactionList.setDate(humanDate(oiltime));
                transactionList.setTime(humanTime(oiltime));
                //
                tl.add(i, transactionList);
            }
            transactionsAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String humanDate(String conv){
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ROOT).parse(conv);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (conv.substring(0,4).matches(String.valueOf(currentYear))){
                stringDate = new SimpleDateFormat("MMM d", Locale.US).format(date).toUpperCase(Locale.ROOT);
            } else {
                stringDate = new SimpleDateFormat("YYYY MMM d", Locale.US).format(date).toUpperCase(Locale.ROOT);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stringDate;
    }
    public String humanTime(String conv){
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ROOT).parse(conv);
            stringTime = new SimpleDateFormat("h:mm a",Locale.ROOT).format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stringTime;
    }
    private class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;
        private SimpleDividerItemDecoration(Context context) {
            mDivider = ContextCompat.getDrawable(context,R.drawable.line_divider);
        }
        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
