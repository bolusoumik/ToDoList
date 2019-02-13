package radiodemo.android.soumik.com.todo_list;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import radiodemo.android.soumik.com.todo_list.Database.DBhelper;

public class MainActivity extends AppCompatActivity {

    DBhelper dBhelper;
    ArrayAdapter<String> mAdapter;
    ListView lstTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dBhelper= new DBhelper(this);

        lstTask=(ListView)findViewById(R.id.tasklist);

        loadTasklist();
    }

    private void loadTasklist()
    {
        ArrayList<String> taskList= dBhelper.getTaskList();
        if(mAdapter==null)
        {
            mAdapter=new ArrayAdapter<String>(this,R.layout.row,R.id.task_title,taskList);
            lstTask.setAdapter(mAdapter);
        }
        else
        {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);

        //Change menu icon color

        Drawable icon=menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_add_task:
                final EditText taskeditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add New Task")
                        .setMessage("What do you want next?")
                        .setView(taskeditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskeditText.getText());
                                dBhelper.insertNewTask(task);
                                loadTasklist();
                            }
                        }).setNegativeButton("Cancel",null).create();
                dialog.show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteTask(View view)
    {
        View parent =(View)view.getParent();
        TextView taskTetView= (TextView)findViewById(R.id.task_title);
        String task=String.valueOf(taskTetView.getText());
        dBhelper.deleteTask(task);
        loadTasklist();
    }
}
