package com.example.superdupercoolplantapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.superdupercoolplantapp.MainActivity;
import com.example.superdupercoolplantapp.R;
import com.example.superdupercoolplantapp.background.models.AccountModel;
import com.example.superdupercoolplantapp.background.viewmodels.ViewModelMain;

public class Account extends Fragment {
    private MainActivity activity;
    private ViewModelMain viewModel;

    private TextView accountUsername;
    private EditText accountName, accountPassword, accountEmail, accountNumber;
    private Button update, logout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) requireActivity();

        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accountUsername = view.findViewById(R.id.account_username);
        accountName = view.findViewById(R.id.account_name);
        accountPassword = view.findViewById(R.id.account_password);
        accountEmail = view.findViewById(R.id.account_email);
        accountNumber = view.findViewById(R.id.account_phone);

        update = view.findViewById(R.id.account_update);

        logout = view.findViewById(R.id.account_logout);

        viewModel = new ViewModelProvider(activity).get(ViewModelMain.class);
        viewModel.getLoggedInAccount().observe(getViewLifecycleOwner(), this::onAccountChanged);

    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setText(getString(R.string.account));
        activity.showBottomNav();
    }

    private void onAccountChanged(AccountModel accountModel) {
        accountUsername.setText(accountModel.getUserName());
        accountName.setText(accountModel.getRealName());
        accountPassword.setText(accountModel.getPassword());
        accountEmail.setText(accountModel.getEmailAddress());
        accountNumber.setText(accountModel.getPhoneNumber());
    }
}