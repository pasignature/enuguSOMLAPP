package com.enugusomlapp.enugusomlapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;


class AsyncGetChildProfile extends AsyncTask<String, String, Cursor> {
    private Context ctx;
    AsyncGetChildProfile(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        // show progress dialog
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Cursor doInBackground(String... params) {
        //publishProgress("Saving Patient Data..."); // Calls onProgressUpdate()
        // 1. download data from the server
        String method = params[0];
        String child_id = params[1];

        // 2. First we have to open our DbHelper class by creating a new object of that
        DbSQLiteHelper androidOpenDbHelperObj = new DbSQLiteHelper(ctx);

        if(method.equals("fetch_childprofile")){
            // Then we need to get a writable SQLite database, because we are going to insert some values
            // SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.
            // 3. get reference to writable DB
            SQLiteDatabase SQliteDB = androidOpenDbHelperObj.getWritableDatabase();
            // //Query db and rturn cursor
            Cursor cursor;
            String checkQuery = "SELECT * FROM registration_children WHERE childID='"+child_id+"' LIMIT 1";
            cursor= SQliteDB.rawQuery(checkQuery,null);
            if(cursor.moveToFirst()){
                return cursor;
            }else{
                return cursor;
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Cursor result){
        //super.onPostExecute(result);
        if(result != null && result.moveToFirst()){
            // extract data from cursor
            String   childID = result.getString(result.getColumnIndex("childID"));
            String   patientcode = result.getString(result.getColumnIndex("regmainFK"));
            String   fullnames = result.getString(result.getColumnIndex("fullname"));
            String   sex = result.getString(result.getColumnIndex("sex"));
            String   dob = result.getString(result.getColumnIndex("dob"));
            String   bloodgroup = result.getString(result.getColumnIndex("bloodgroup"));
            String   genotype = result.getString(result.getColumnIndex("genotype"));
            String   HIVstatus = result.getString(result.getColumnIndex("HIVstatus"));
            String   createdby = result.getString(result.getColumnIndex("createdby"));
            String   datetime = result.getString(result.getColumnIndex("datetime"));
            String   lastupdate = result.getString(result.getColumnIndex("lastupdate"));
            String   lastupdateby = result.getString(result.getColumnIndex("lastupdateby"));

            //concatenate strings
            String profiledata = "Gender: "+sex+"\n" +
                    "Date of birth: "+dob+"\n" +
                    "Blood Group: "+bloodgroup+"\n" +
                    "GenoType: " + genotype + "\n" +
                    "HIV Status: " + HIVstatus + "\n" +
                    "Created By: " + createdby + "\n" +
                    "Date / Time: " + datetime + "\n" +
                    "Last Update: " + lastupdate + "\n" +
                    "Last Updateby: " + lastupdateby + "\n" +
                    "DB ID: " + childID + "\n";

            //Toast.makeText(ctx.getApplicationContext(), "Congrats, Child was inserted successfully.", Toast.LENGTH_LONG).show();

            // 5. redirect to childForm success page
            Intent intent = new Intent(ctx, SuccessChildFormActivity.class);
            intent.putExtra("sex", sex);
            intent.putExtra("patientcode", patientcode);
            intent.putExtra("childID", childID);
            intent.putExtra("fullnames", fullnames);
            intent.putExtra("profiledata", profiledata);
            ctx.startActivity(intent);
            // 4. close
            result.close();

        }else{
            Toast.makeText(ctx.getApplicationContext(), "ERROR: Data retrieval Failed. Pls try again.", Toast.LENGTH_LONG).show();
        }


        // 6. dismiss dialog if available
    }

}