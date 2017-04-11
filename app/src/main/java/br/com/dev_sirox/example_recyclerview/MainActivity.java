package br.com.dev_sirox.example_recyclerview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView imgADD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        imgADD = (ImageView) findViewById(R.id.imgADD);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        final ArrayList<String> items = new ArrayList<>();
        items.add("test1");
        items.add("test2");
        items.add("test3");
        items.add("test4");

        //Escuta remoção do clique e notifica o adapter a alteração
        mAdapter = new MyAdapter(this, items, new MyAdapter.IMyAdapterListener() {
            @Override
            public void onRemove(final int pos) {
                Log.i("SIZE", String.valueOf(items.size()));

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog
                        .setTitle("Remoção")
                        .setMessage("Tem certeza nesta ação?")
                        .setNeutralButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Ação Cancelada!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //remove(name);
                                //delegate.onRemove(position);
                                items.remove(pos);
                                mAdapter.notifyItemRemoved(pos);
                            }
                        });
                alertDialog.show();
            }
        });

        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        alertDialog
                                .setTitle("Remoção")
                                .setMessage("Tem certeza nesta ação?")
                                .setNeutralButton("Não", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(MainActivity.this, "Ação Cancelada!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        items.remove(viewHolder.getAdapterPosition());
                                        mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                                    }
                                });
                        alertDialog.show();
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        imgADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creates layout for AlertDialog
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_login_alert, null);

                final EditText et_item = (EditText) dialogView.findViewById(R.id.et_item);

                AlertDialog.Builder alertLogin = new AlertDialog.Builder(MainActivity.this)
                        .setView(dialogView)
                        .setTitle("Adicionar Item na Lista")
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                items.add(et_item.getText().toString());
                                mAdapter.notifyDataSetChanged();
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog alertLoginBuilder = alertLogin.create();
                alertLoginBuilder.show();
                //alertLoginBuilder.getWindow().setLayout(600, 700); //Controlling width and height.
            }
        });

    }
}
