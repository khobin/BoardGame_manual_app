package com.example.kimhobin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    final static int STARTEGY = 1, MAPIA = 2, SENSE = 3, MANIA = 4, BRAIN = 5, LUCK = 6;
    ArrayList<Item> list, filteredList;
    EditText txtSearch;
    Button btnName, btnRating, btnPeople, btnReset;
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerDecoration spaceDecoration;
    RadioButton[] radioButtons = new RadioButton[6];
    Integer[] radioBtnIds = {R.id.cb1, R.id.cb2, R.id.cb3, R.id.cb4,
            R.id.cb5, R.id.cb6};
    boolean isFiltered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("201768020김호빈");

        txtSearch = (EditText) findViewById(R.id.editText);
        btnName = (Button) findViewById(R.id.btnName);
        btnRating = (Button) findViewById(R.id.btnRating);
        btnPeople = (Button) findViewById(R.id.btnPeople);
        btnReset = (Button) findViewById(R.id.btnReset);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        for (int i = 0; i < radioBtnIds.length; i++) {
            radioButtons[i] = (RadioButton) findViewById(radioBtnIds[i]);
        }

        list = new ArrayList<>();
        filteredList = new ArrayList<>();
        createList(list);

        spaceDecoration = new RecyclerDecoration(20);
        recyclerView.addItemDecoration(spaceDecoration);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter(list, this);
        recyclerView.setAdapter(adapter);

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = txtSearch.getText().toString();
                search(searchText);
            }
        });
        btnName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!isFiltered) {
                    Collections.sort(list, new Comparator<Item>() {
                        @Override
                        public int compare(Item o1, Item o2) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        }
                    });
                    adapter.filterList(list);
                } else {
                    Collections.sort(filteredList, new Comparator<Item>() {
                        @Override
                        public int compare(Item o1, Item o2) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        }
                    });
                    adapter.filterList(filteredList);
                }

            }
        });
        btnRating.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!isFiltered) {
                    Collections.sort(list, new Comparator<Item>() {
                        @Override
                        public int compare(Item o1, Item o2) {
                            if (o1.getRating() > o2.getRating()) {
                                return 1;
                            } else if (o1.getRating() < o2.getRating()) {
                                return -1;
                            } else
                                return 0;
                        }
                    });
                    Collections.reverse(list);
                    adapter.filterList(list);
                } else {
                    Collections.sort(filteredList, new Comparator<Item>() {
                        @Override
                        public int compare(Item o1, Item o2) {
                            if (o1.getRating() > o2.getRating()) {
                                return 1;
                            } else if (o1.getRating() < o2.getRating()) {
                                return -1;
                            } else
                                return 0;
                        }
                    });
                    Collections.reverse(filteredList);
                    adapter.filterList(filteredList);
                }

            }
        });

        btnPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("인원 수 선택");
                dlg.setIcon(R.drawable.hobin);
                final String[] peopleArray = new String[]{"2-3인", "4인", "5인 이상"};

                dlg.setItems(peopleArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //아이템 클릭 시 동작
                        for (int i = 0; i < radioBtnIds.length; i++) {
                            txtSearch.setText(null);
                            radioButtons[i].setChecked(false);
                        }
                        filtering(peopleArray[which]);
                    }
                });
                dlg.show();

            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 초기화
                for (int i = 0; i < radioBtnIds.length; i++) {
                    txtSearch.setText(null);
                    radioButtons[i].setChecked(false);
                }
                list.clear();
                createList(list);
                adapter.filterList(list);
                isFiltered = false;
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Item item = list.get(position);
                Intent intent = new Intent(MainActivity.this, YoutubePlayer.class);

                intent.putExtra("title", item.getTitle());
                intent.putExtra("url", item.getUrl());

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private MainActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public void createList(ArrayList<Item> list) {

        list.add(new Item("할리갈리", "img1", "2-3인", LUCK, "nQ3fw-CXIg4", 4));
        list.add(new Item("우봉고", "img2", "4인", BRAIN, "eMOTzy79Z30", 3.5));
        list.add(new Item("루미큐브", "img3", "5인 이상", BRAIN, "uHuYRzgzbL8", 3));
        list.add(new Item("다빈치 코드", "img4", "4인", MAPIA, "ElNpDx2oKpQ", 1.5));
        list.add(new Item("쿼리도", "img5", "2-3인", BRAIN, "aOi496lTHOU", 2.5));

        list.add(new Item("뱅 BANG!", "img6", "5인 이상", MAPIA, "DcI7tsUoCnM", 5));
        list.add(new Item("워필드", "img7", "4인", STARTEGY, "dX0x8NFRnMk", 4.5));
        list.add(new Item("펭귄 얼음 깨기", "img8", "2-3인", SENSE, "dX0x8NFRnMk", 1.5));
        list.add(new Item("샤오리아", "img9", "4인", STARTEGY, "zJg29gtVU7I", 2.5));
        list.add(new Item("7원더스", "img10", "2-3인", STARTEGY, "HBUMGsAP4Gc", 3.5));

        list.add(new Item("CLUE 클루", "img11", "5인 이상", MAPIA, "7Ium0jpht0g", 4));
        list.add(new Item("챠오챠오", "img12", "2-3인", LUCK, "ZHoLj040vtg", 2.5));
        list.add(new Item("레지스탕스 Avalon", "img13", "5인 이상", MANIA, "C2feZBsDHlY", 3.5));
        list.add(new Item("팬데믹", "img14", "4인", MANIA, "urllnrCU_G0", 4.5));
        list.add(new Item("텀블링 몽키", "img15", "2-3인", SENSE, "XM2MS0-9jr8", 1));
    }

    public void search(String filter) {
        filteredList.clear();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTitle().toLowerCase().contains(filter.toLowerCase())) {
                filteredList.add(list.get(i));
            }
        }
        adapter.filterList(filteredList);
        isFiltered = true;
    }

    public void filtering(int filter) {
        filteredList.clear();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getGenre() == filter) {
                filteredList.add(list.get(i));
            }
        }
        adapter.filterList(filteredList);
        isFiltered = true;

    }

    public void filtering(String filter) {
        filteredList.clear();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPeople().equals(filter)) {
                filteredList.add(list.get(i));
            }
        }
        adapter.filterList(filteredList);
        isFiltered = true;

    }

    public void RadioButtonListener(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        int filter;
        switch (view.getId()) {
            case R.id.cb1:
                if (checked) {
                    filter = 1;
                    filtering(filter);
                }
                break;
            case R.id.cb2:
                if (checked) {
                    filter = 2;
                    filtering(filter);
                }
                break;
            case R.id.cb3:
                if (checked) {
                    filter = 3;
                    filtering(filter);
                }
                break;
            case R.id.cb4:
                if (checked) {
                    filter = 4;
                    filtering(filter);
                }
                break;
            case R.id.cb5:
                if (checked) {
                    filter = 5;
                    filtering(filter);
                }
                break;
            case R.id.cb6:
                if (checked) {
                    filter = 6;
                    filtering(filter);
                }
                break;
            default:
                adapter.filterList(list);
                break;

        }
    }
}