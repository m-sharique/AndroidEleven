package com.example.eleven;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DbActivity extends AppCompatActivity {

    private EditText editTextId, editTextName, editTextDuration;
    private Button buttonInsert, buttonUpdate, buttonDelete, buttonViewAll;
    private DatabaseReference databaseCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_db);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        databaseCourses = FirebaseDatabase.getInstance().getReference("courses");

        editTextId = findViewById(R.id.editTextId);
        editTextName = findViewById(R.id.editTextName);
        editTextDuration = findViewById(R.id.editTextDuration);
        buttonInsert = findViewById(R.id.buttonInsert);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonViewAll = findViewById(R.id.buttonViewAll);

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourse();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCourse();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCourse();
            }
        });

        buttonViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAllCourses();
            }
        });
    }

    private void addCourse() {
        String id = editTextId.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String duration = editTextDuration.getText().toString().trim();

        if (!id.isEmpty() && !name.isEmpty() && !duration.isEmpty()) {
            Course course = new Course(id, name, duration);
            databaseCourses.child(id).setValue(course);
            showAlert("Success", "Course Added");
        } else {
            showAlert("Error", "Please fill all fields");
        }
    }

    private void updateCourse() {
        String id = editTextId.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String duration = editTextDuration.getText().toString().trim();

        if (!id.isEmpty() && !name.isEmpty() && !duration.isEmpty()) {
            Course course = new Course(id, name, duration);
            databaseCourses.child(id).setValue(course);
            showAlert("Success", "Course Updated");
        } else {
            showAlert("Error", "Please fill all fields");
        }
    }

    private void deleteCourse() {
        String id = editTextId.getText().toString().trim();

        if (!id.isEmpty()) {
            databaseCourses.child(id).removeValue();
            showAlert("Success", "Course Deleted");
        } else {
            showAlert("Error", "Please enter a course ID");
        }
    }

    private void viewAllCourses() {
        databaseCourses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    StringBuilder buffer = new StringBuilder();
                    for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                        Course course = courseSnapshot.getValue(Course.class);
                        buffer.append("ID: ").append(course.getCourseId()).append("\n");
                        buffer.append("Name: ").append(course.getCourseName()).append("\n");
                        buffer.append("Duration: ").append(course.getCourseDuration()).append("\n\n");
                    }
                    showAlert("Courses", buffer.toString());
                } else {
                    showAlert("Error", "No Courses Found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showAlert("Error", databaseError.getMessage());
            }
        });
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DbActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}