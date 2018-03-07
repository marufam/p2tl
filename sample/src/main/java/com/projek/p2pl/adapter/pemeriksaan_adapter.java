package com.projek.p2pl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projek.p2pl.R;
import com.projek.p2pl.model.m_barangbukti;
import com.projek.p2pl.model.m_pelanggan;
import com.projek.p2pl.model.m_periksa;
import com.projek.p2pl.model.m_petugas;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;


public class pemeriksaan_adapter extends RecyclerView.Adapter<pemeriksaan_adapter.ViewHolder> implements RealmChangeListener {

    private RealmResults<m_pelanggan> items;
    private RealmResults<m_petugas> mypetugas;
    private RealmResults<m_periksa> myperiksa;
//    private RealmResults<m_barangbukti>  mybarangbukti;
    Context a;

    public pemeriksaan_adapter(RealmResults<m_pelanggan> items, RealmResults<m_petugas> mypetugas, RealmResults<m_periksa> myperiksa,  Context a) {
        this.items = items;
        this.mypetugas = mypetugas;
        this.myperiksa = myperiksa;
//        this.mybarangbukti = mybarangbukti;
        items.addChangeListener(this);
        this.a = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_pemeriksaan,viewGroup,false);
        return new pemeriksaan_adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final m_pelanggan model = items.get(i);
        final m_petugas model1 = mypetugas.get(i);
        final m_periksa model2 = myperiksa.get(i);

        final String nama = model.getNama();
        final String deskripsi = model2.getDeskripsi_pelanggaran();
        final String petugas = model1.getNama();
        final String tindakan = model2.getTindakan();

        viewHolder.nama.setText(nama);
        viewHolder.tdeskripsi.setText((deskripsi));
        viewHolder.ttindakan.setText((tindakan));
        viewHolder.tpetugas.setText((petugas));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onChange() {
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, tdeskripsi, tpetugas, ttindakan;
        public ViewHolder(View itemView) {
            super(itemView);
            nama = (TextView) itemView.findViewById(R.id.nama);
            tdeskripsi = (TextView) itemView.findViewById(R.id.t_deskripsi);
            tpetugas = (TextView) itemView.findViewById(R.id.t_namapetugas);
            ttindakan = (TextView) itemView.findViewById(R.id.t_tindakan);

        }
    }
}
