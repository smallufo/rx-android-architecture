package com.tehmou.rxbookapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tehmou.rxbookapp.data.DataStore;
import com.tehmou.rxbookapp.utils.SubscriptionManager;
import com.tehmou.rxbookapp.viewmodels.BookViewModel;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by ttuo on 19/03/14.
 */
public class BookFragment extends Fragment {

    final private SubscriptionManager subscriptionManager = new SubscriptionManager();
    private BookViewModel bookViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookViewModel = new BookViewModel(DataStore.getInstance(), "436346");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView bookNameTextView = (TextView) view.findViewById(R.id.book_name);
        subscriptionManager.add(subscribeTextView(bookViewModel.getBookName(), bookNameTextView));

        final TextView bookAuthorTextView = (TextView) view.findViewById(R.id.book_author);
        subscriptionManager.add(subscribeTextView(bookViewModel.getAuthorName(), bookAuthorTextView));

        final TextView bookPriceTextView = (TextView) view.findViewById(R.id.book_price);
        subscriptionManager.add(subscribeTextView(bookViewModel.getBookPrice(), bookPriceTextView));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscriptionManager.unsubscribeAll();
    }

    static private Subscription subscribeTextView(Observable<String> observable, final TextView textView) {
        return observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        textView.setText(s);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        bookViewModel.subscribeToDataStore();
    }

    @Override
    public void onPause() {
        super.onPause();
        bookViewModel.unsubscribeFromDataStore();
    }

}