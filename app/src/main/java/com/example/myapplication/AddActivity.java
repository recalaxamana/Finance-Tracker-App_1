package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityAddBinding;

public class AddActivity extends AppCompatActivity {
    private ActivityAddBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        boolean update = getIntent().getBooleanExtra("update", false);
        Toast.makeText(this, "" + update, Toast.LENGTH_SHORT).show();
        String desc = getIntent().getStringExtra("desc");
        long amount = getIntent().getLongExtra("amount", -1);
        int id = getIntent().getIntExtra("id", -1);
        String paymenttype = getIntent().getStringExtra("paymenttype");
        boolean isIncome = getIntent().getBooleanExtra("isIncome", false);
        if (update) {
            binding.addText.setText("Update");
            binding.amount.setText(amount + "");
            binding.paymentType.setText(paymenttype);
            binding.description.setText(desc);

            if (isIncome) {
                binding.expenseRadio.setChecked(true);
            } else {
                if (isIncome) {
                    binding.incomeRadio.setChecked(true);
                }
            }

            binding.addBtn.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    String amount = binding.amount.getText().toString();
                    String type = binding.paymentType.getText().toString();
                    String desc = binding.description.getText().toString();

                    ExpenseTable expenseTable = new ExpenseTable();

                    expenseTable.setAmount(Long.parseLong(amount));
                    expenseTable.setDescription(desc);
                    expenseTable.setIncome(isIncome);
                    expenseTable.setPaymentType(type);

                    ExpenseDatabase expenseDatabase = ExpenseDatabase.getInstance(view.getContext());
                    ExpenseDao expenseDao = expenseDatabase.getDao();
                    if (update) {
                        expenseDao.insertExpense(expenseTable);
                    } else {
                        expenseTable.setId(id);
                        expenseDao.updateExpense(expenseTable);
                    }
                    finish();
                }
            });


        }

    }
}