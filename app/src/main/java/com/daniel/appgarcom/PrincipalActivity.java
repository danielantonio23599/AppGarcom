package com.daniel.appgarcom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.daniel.appgarcom.fragment.MesaFragment;
import com.daniel.appgarcom.fragment.MesasFragment;
import com.daniel.appgarcom.fragment.PerfilFragment;
import com.daniel.appgarcom.modelo.beans.Usuario;
import com.daniel.appgarcom.modelo.persistencia.BdUsuario;
import com.daniel.appgarcom.util.UtilImageTransmit;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView perfil;
    private TextView nome, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View view = navigationView.inflateHeaderView(R.layout.nav_header_principal);
        perfil = (ImageView) view.findViewById(R.id.imPerfil);
        nome = (TextView) view.findViewById(R.id.tvUser);
        email = (TextView) view.findViewById(R.id.tvEmail);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        setDados();
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new PerfilFragment());
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                }
            }
        });
    }

    private void setDados() {
        BdUsuario bdUsuario = new BdUsuario(getApplicationContext());
        Usuario u = bdUsuario.listar();
        //fecha conexao
        bdUsuario.close();
        Bitmap bitmap = UtilImageTransmit.convertBytetoImage(u.getFoto());
        if (bitmap != null) {
            perfil.setImageBitmap(bitmap);
        }
        nome.setText(u.getNome());
        email.setText(u.getEmail());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
        }
    }

    private void mudaActivity(final Class classe) {
        Log.i("[IFMG]", "passou no muda actyvity" + classe.getName());
        final Intent intent = new Intent(this, classe);
        startActivity(intent);
    }


  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_item, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter.getFilter().filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            replaceFragment(new PerfilFragment());
        } else if (id == R.id.nav_vendas) {
            replaceFragment(new MesaFragment());
            getSupportActionBar().setTitle("Mesas");


        } else if (id == R.id.nav_pedido) {
            // replaceFragment(new MesasFragment());


        } else if (id == R.id.nav_cancelar) {
            // replaceFragment(new MesasFragment());


        } else if (id == R.id.nav_cardapio) {
            // replaceFragment(new MesasFragment());


        } else if (id == R.id.nav_produtos) {
            // replaceFragment(new MesasFragment());


        } else if (id == R.id.nav_promocao) {
            // replaceFragment(new MesasFragment());


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment, "IFMG").addToBackStack(null).commit();
    }
}
