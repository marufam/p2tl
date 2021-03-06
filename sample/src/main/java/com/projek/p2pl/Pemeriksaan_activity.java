package com.projek.p2pl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.projek.p2pl.adapter.pemeriksaan_adapter;
import com.projek.p2pl.model.m_barangbukti;
import com.projek.p2pl.model.m_pelanggan;
import com.projek.p2pl.model.m_periksa;
import com.projek.p2pl.model.m_petugas;

import io.realm.Realm;

public class Pemeriksaan_activity extends AppCompatActivity {


    private RecyclerView mRecycleview;
    private RecyclerView.LayoutManager mLayoutmanager;
    private RecyclerView.Adapter mAdapter;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemeriksaan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("List Pemeriksaan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRealm = Realm.getInstance(getApplicationContext());
        mRecycleview = (RecyclerView) findViewById(R.id.tbpemeriksaan);
        mLayoutmanager = new LinearLayoutManager(getApplicationContext());
        mRecycleview.setLayoutManager(mLayoutmanager);

//        Toast.makeText(this, "ukuran : "+mRealm.allObjects(m_pelanggan.class).size(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "ukuran : "+mRealm.allObjects(m_petugas.class).size(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "ukuran : "+mRealm.allObjects(m_barangbukti.class).size(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "ukuran : "+mRealm.allObjects(m_periksa.class).size(), Toast.LENGTH_SHORT).show();

        mAdapter = new pemeriksaan_adapter(mRealm.allObjects(m_pelanggan.class),mRealm.allObjects(m_petugas.class),mRealm.allObjects(m_periksa.class),mRealm.allObjects(m_barangbukti.class),getApplicationContext());
        mAdapter.notifyDataSetChanged();
        mRecycleview.setAdapter(mAdapter);
        Log.d("isi",mRealm.allObjects(m_pelanggan.class).toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

}
