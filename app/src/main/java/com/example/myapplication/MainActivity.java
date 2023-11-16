package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.ActivityMainBinding;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ClickEvent {
    private ActivityMainBinding binding;
    RecyclerView recyclerView;
    ExpenseAdapter expenseAdapter;
    List<ExpenseTable> expenseTableList;
    ExpenseDatabase expenseDatabase;
    ExpenseDao expenseDao;
    long expense=0, income=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.newBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        })
    ;}

    @Override
    protected void onResume() {
        super.onResume();
        expenseDatabase = ExpenseDatabase.getInstance(this);
        expenseDao = expenseDatabase.getDao();
        expenseAdapter = new ExpenseAdapter(this, this);
        binding.itemsRecycler.setAdapter(expenseAdapter);
        binding.itemsRecycler.setLayoutManager(new LinearLayoutManager(this));

        List<ExpenseTable> expenseTables = expenseDao.getAll();


        for (int i = 0; i < expenseTables.size(); i++) {
            if (expenseTables.get(i).isIncome()) {
                income= income+expenseTables.get(i).getAmount();
            } else {
                expense= expense+expenseTables.get(i).getAmount();
            }
            expenseAdapter.add(expenseTables.get(i));
        }

        binding.totalExpense.setText(expense+"");
        binding.totalIncome.setText(income+"");
        long balance = income - expense;
        binding.totalAmount.setText(balance+"");
    }


    @Override
    public void OnClick(int pos) {

        Intent intent=new Intent(MainActivity.this, AddActivity.class);
        intent.putExtra("update", true);
        intent.putExtra("id", expenseAdapter.getId(pos));
        intent.putExtra("desc", expenseAdapter.desc(pos));
        intent.putExtra("paymenttype", expenseAdapter.paymentType(pos));
        intent.putExtra("amount", expenseAdapter.amount(pos));
        intent.putExtra("isIncome", expenseAdapter.isIncome(pos));
        startActivity(intent);
    }


    @Override
    public void OnLongPress(int pos) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete it?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int id = expenseAdapter.getId(pos);
                expenseDao.delete(id);
                expenseAdapter.delete(pos);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();

    }
}