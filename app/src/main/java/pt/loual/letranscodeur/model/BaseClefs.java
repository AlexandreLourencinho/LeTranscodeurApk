package pt.loual.letranscodeur.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

import pt.loual.letranscodeur.outils.GenClef;

public class BaseClefs extends SQLiteOpenHelper implements DAOinterface<Clefs> {

    public static final String  BASE_CLEF = "baseClefs";
    public static final String  TABLE_CLEFS = "tableClefs";
    public static final String  COLONNE_ID = "clef_ID";
    public static final String  COLONNE_NOM = "clef_nom";
    public static final String  COLONNE_CONTENU = "clef_contenu";

    public BaseClefs(Context contexte)
    {
        super(contexte,BASE_CLEF,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_CLEFS+"("+COLONNE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
        +COLONNE_NOM+" TEXT UNIQUE NOT NULL, "+COLONNE_CONTENU+" TEXT NOT NULL )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_CLEFS);
        onCreate(sqLiteDatabase);
    }


    public void creerDefaut()
    {
        int nombre = this.compteur();
        if(nombre==0){
            GenClef keyGen = new GenClef();
            Clefs clef0 = new Clefs(0,"Selectionnez une clef", "");
            Clefs clef1 = new Clefs(1,"Première clef", keyGen.randomKey());
            this.ajouter(clef0);
            this.ajouter(clef1);
        }

    }


    @Override
    public HashMap<Boolean,String> ajouter(Clefs o) {
        HashMap<Boolean,String> list = new HashMap<>();
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues valeurs = new ContentValues();
            valeurs.put(COLONNE_NOM,o.getNom());
            valeurs.put(COLONNE_CONTENU,o.getContenu());
            db.insert(TABLE_CLEFS,null,valeurs);
            list.put(true,null);
            return list;
        } catch (Exception e) {
            list.put(false,e.toString());
            return list;
        }

    }

    @Override
    public HashMap<Boolean,Object> modifier(Clefs o) {
        HashMap<Boolean,Object> list = new HashMap<>();
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues valeurs = new ContentValues();
            valeurs.put(COLONNE_NOM,o.getNom());
            valeurs.put(COLONNE_CONTENU,o.getContenu());
            db.update(TABLE_CLEFS,valeurs,COLONNE_ID+"=?",new String[]{String.valueOf(o.getId())});
            list.put(true,null);
            return list;
        } catch (Exception e) {
            list.put(false,e);
            return list;
        }

    }

    @Override
    public HashMap<Boolean,Object> supprimer(Clefs o) {
        HashMap<Boolean,Object> list = new HashMap<>();
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.delete(TABLE_CLEFS,COLONNE_ID+"=?",new String[]{String.valueOf(o.getId())});
            list.put(true,null);
            return list;
        } catch (Exception e) {
            list.put(false,e);
            return list;
        }


    }

    @Override
    public ArrayList<Clefs> liste() {
        ArrayList<Clefs> listeClefs = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_CLEFS, null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getColumnIndex(COLONNE_ID);
                int nom = cursor.getColumnIndex(COLONNE_NOM);
                int contenu = cursor.getColumnIndex(COLONNE_CONTENU);


                Clefs clef = new Clefs();
                clef.setId(cursor.getInt(id));
                clef.setNom(cursor.getString(nom));
                clef.setContenu(cursor.getString(contenu));
                listeClefs.add(clef);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return listeClefs;
    }

    @Override
    public Clefs trouverUn(Clefs o) {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_CLEFS,new String[]{COLONNE_ID,COLONNE_NOM,COLONNE_CONTENU},
                COLONNE_ID+"=?",new String[]{String.valueOf(o.getId())},null,null,null);
        if(cursor!=null && cursor.moveToFirst()){
            int id = cursor.getColumnIndex(COLONNE_ID);
            int nom = cursor.getColumnIndex(COLONNE_NOM);
            int contenu = cursor.getColumnIndex(COLONNE_CONTENU);


            cursor.moveToFirst();
            Clefs clef = new Clefs();
            clef.setId(cursor.getInt(id));
            clef.setNom(cursor.getString(nom));
            clef.setContenu(cursor.getString(contenu));
            cursor.close();
            return clef;
        }else{
            return null;
        }
    }

    public int compteur(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_CLEFS,null);
        int nombre = cursor.getCount();
        cursor.close();
        return nombre;
    }

}
