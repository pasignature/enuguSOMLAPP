package com.enugusomlapp.enugusomlapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;


class AsyncGetVisitHistory extends AsyncTask<String, String, Cursor> {
    private Context ctx;
    AsyncGetVisitHistory(Context ctx) {
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
        String visitid = params[1];

        // 2. First we have to open our DbHelper class by creating a new object of that
        DbSQLiteHelper androidOpenDbHelperObj = new DbSQLiteHelper(ctx);

        if(method.equals("fetch_visitHistory")){
            // Then we need to get a writable SQLite database, because we are going to insert some values
            // SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.
            // 3. get reference to writable DB
            SQLiteDatabase SQliteDB = androidOpenDbHelperObj.getWritableDatabase();
            // //Query db and rturn cursor
            Cursor cursor;
            String checkQuery = "SELECT * FROM registration_visithistory WHERE historyID='"+visitid+"' LIMIT 1";
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
            String   historyID = result.getString(result.getColumnIndex("historyID"));
            //String   regmainFK = result.getString(result.getColumnIndex("regmainFK"));
            //String   childFK = result.getString(result.getColumnIndex("childFK"));
            String   health_facility = result.getString(result.getColumnIndex("health_facility"));
            String   facility_street = result.getString(result.getColumnIndex("facility_street"));
            String   citystate = result.getString(result.getColumnIndex("citystate"));
            String   facility_tel = result.getString(result.getColumnIndex("facility_tel"));
            String   visitreason = result.getString(result.getColumnIndex("visitreason"));
            String   consultantdoctor = result.getString(result.getColumnIndex("consultantdoctor"));
            String   collected_items = result.getString(result.getColumnIndex("collected_items"));
            String   comments = result.getString(result.getColumnIndex("comments"));
            String   next_appointment = result.getString(result.getColumnIndex("next_appointment"));
            String   visitdatetime = result.getString(result.getColumnIndex("visitdatetime"));

            //concatenate strings
            String profiledata = "Street: "+facility_street+"\n" +
                    "State: "+citystate+"\n" +
                    "Telephone: " + facility_tel + "\n" +
                    "Reason for visit:\n" + visitreason + "\n\n" +
                    "Consultant Doctor: " + consultantdoctor + "\n" +
                    "Items Collected:\n" + collected_items + "\n\n" +
                    "Remarks/Comments:\n" + comments + "\n\n" +
                    "Date of Next Appointment: " + next_appointment + "\n" +
                    "Visitation Date/Time: " + visitdatetime + "\n" +
                    "DB ID: " + historyID + "\n";

            //Toast.makeText(ctx.getApplicationContext(), "Congrats, Child was inserted successfully.", Toast.LENGTH_LONG).show();

            // 5. redirect to visits success page
            Intent intent = new Intent(ctx, SuccessVisitFormActivity.class);
            intent.putExtra("historyID", historyID);
            intent.putExtra("health_facility", health_facility);
            intent.putExtra("facility_street", facility_street);
            intent.putExtra("citystate", citystate);
            intent.putExtra("profiledata", profiledata);
            ctx.startActivity(intent);
            // 4. close
            result.close();

        }else{
            Toast.makeText(ctx.getApplicationContext(), "ERROR: Visit History retrieval Failed. Pls try again.", Toast.LENGTH_LONG).show();
        }


        // 6. dismiss dialog if available

    }

}