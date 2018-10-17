package com.example.antonio.bdproductos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText et_codigo, et_descripcion, et_precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_codigo = findViewById(R.id.txt_codigo);
        et_descripcion = findViewById(R.id.txt_descripcion);
        et_precio = findViewById(R.id.txt_precio);
    }

    /*
     * Manejadores de los botones
     */

    // Método para registrar un producto
    public void registrar(View v) {
        SQLiteDatabase database = cargarHelper();

        ContentValues values = toContentValues();

        if(values != null) {
            database.insert("productos", null, values);

            vaciar_ets();

            mostrarToast("El producto ha sido registrado");
        } else {
            mostrarToast("Faltan datos por rellenar");
        }
        database.close();
    }

    // Método para buscar producto
    public void buscar(View v) {
        SQLiteDatabase database = cargarHelper();

        String codigo = et_codigo.getText().toString();

        if(!codigo.isEmpty()) {
            Cursor fila = database.rawQuery("select descripcion, precio from productos where codigo = " + codigo, null);

            if(fila.moveToFirst()) {
                et_descripcion.setText(fila.getString(fila.getColumnIndex("descripcion"))); // ó se puede poner et_descripcion.setText(fila.getString(0));
                et_precio.setText(fila.getString(fila.getColumnIndex("precio"))); // ó se puede poner et_precio.setText(fila.getString(1));
            } else {
                mostrarToast("El producto no existe");
            }
        } else {
            mostrarToast("Faltan datos por rellenar");
        }
        database.close();
    }

    //Método para eliminar producto
    public void eliminar(View v) {
        SQLiteDatabase database = cargarHelper();

        String codigo = et_codigo.getText().toString();

        if(!codigo.isEmpty()) {
            int cantidad = database.delete("productos", "codigo = " + codigo , null);

            if(cantidad == 1) {
                mostrarToast("El elemento ha sido borrado");
            } else {
                mostrarToast("El producto no existe");
            }

            vaciar_ets();
        } else {
            mostrarToast("Faltan datos por rellenar");
        }
        database.close();
    }

    //Método para modificar producto
    public void modificar(View v) {
        SQLiteDatabase database = cargarHelper();

        ContentValues values = toContentValues();

        if(values != null) {
            int cantidad = database.update("productos", values, "codigo = " + values.getAsString("codigo"), null);

            if(cantidad == 1) {
                mostrarToast("El producto ha sido modificado");
            } else {
                mostrarToast("El producto no existe");
            }

            vaciar_ets();
        } else {
            mostrarToast("Faltan datos por rellenar");
        }
    }

    /*
     * Funciones auxiliares
     */

    //Método para cargar la base de datos
    public SQLiteDatabase cargarHelper() {
        AdminSQLiteOpenHelper helper = new AdminSQLiteOpenHelper(this, "gestion", null, 1);
        return helper.getWritableDatabase();
    }

    //Método para vaciar los EditTexts
    public void vaciar_ets() {
        et_codigo.setText("");
        et_descripcion.setText("");
        et_precio.setText("");
    }

    //Método para crear una fila
    public ContentValues toContentValues(){
        String codigo = et_codigo.getText().toString();
        String descripcion = et_descripcion.getText().toString();
        String precio = et_precio.getText().toString();

        if(!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()) {
            ContentValues values = new ContentValues();

            values.put("codigo", codigo);
            values.put("descripcion", descripcion);
            values.put("precio", precio);

            return values;
        } else {
            return null;
        }
    }

    //Método para mostrar un Toast
    public void mostrarToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
