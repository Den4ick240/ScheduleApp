package com.nsu.ccfit.nsuschedule;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nsu.ccfit.nsuschedule.data.DataController;
import com.nsu.ccfit.nsuschedule.data.parser.WeekDay;
import com.nsu.ccfit.nsuschedule.data.wrappers.Data;
import com.nsu.ccfit.nsuschedule.data.wrappers.TimeIntervalData;
import com.nsu.ccfit.nsuschedule.scheduleabstract.Parity;
import com.nsu.ccfit.nsuschedule.scheduleabstract.ScheduleItem;

import net.fortuna.ical4j.data.ParserException;

import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataViewModel extends AndroidViewModel {
    DataController dataController = null;
    List<MutableLiveData<ScheduleItem[]>> schedules;

    public DataViewModel(@NonNull Application application) {
        super(application);
        schedules = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            schedules.add(new MutableLiveData<ScheduleItem[]>());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean loadData() throws IOException, ParserException {
        dataController = new DataController(getApplication().getFilesDir());
        try {
            if (!dataController.loadNSUServerData()) {
                dataController = null;
                return false;
            }
            System.out.println(dataController.getData());
        } catch (IOException | ParserException e) {
            e.printStackTrace();
        }
        updateSchedules();
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateSchedules() throws IOException, ParserException {
        /*TODO: Арина напиши эту функцию пожалустаа
        надо чтоб она испольщовала объект dataController и с помощью него заполняла
        поле schedules
        там список, количество элементов которого по идее должно соответствовать количеству дней.
        Для каждого schedule[i] делаешь .put() и туда передаешь массив с занятиями на i-ый день
        массив я  сделал типа com.nsu.ccfit.nsuschedule.scheduleabstract.ScheduleItem
        можешь наследовать один из своих объектов от этого интерфейса так будет быстрее скорее всего
        или можешь написать нвый класс обертку который будет наследваться от этого интерфейса это будет дольше
        и это будет тебя раздражать так что предыдущий вариант. Можно еще как нибудь анонимными классами обойтись
        которыйе будут наследоваться от этого интерфейся и исользовать stream().map().collect() и в map создавать
        этот анонимный класс, так тоже должно быть нормально.
        я пока что тут ниже всякую фиготень напишу чтоб потестить что работает ты потом это все убери

            ScheduleItem i = new ScheduleItem() {
                @Override
                public String getDescription() {
                    return "Пара";
                }

                @Override
                public String getLocation() {
                    return "Vtnjkdfsn";
                }

                @Override
                public String getSummary() {
                    return "Тут препод какой  тоылоооло";
                }

                @Override
                public String getStartTime() {
                    return "началотУт";
                }

                @Override
                public String getEndTime() {
                    return "а тут конец";
                }

                @Override
                public Parity getParity() {
                    return Parity.ALL;
                }

                @Override
                public boolean isVisible() {
                    return true;
                }
            };
            ScheduleItem i2 = new ScheduleItem() {
                @Override
                public String getDescription() {
                    return "Вторая пара";
                }

                @Override
                public String getLocation() {
                    return "Другое место";
                }

                @Override
                public String getSummary() {
                    return "Иванович ивановИч";
                }

                @Override
                public String getStartTime() {
                    return "10:60";
                }

                @Override
                public String getEndTime() {
                    return "60:10";
                }

                @Override
                public Parity getParity() {
                    return Parity.EVEN;
                }

                @Override
                public boolean isVisible() {
                    return true;
                }
            };

         */
        Data data = dataController.getData();
        for (int i = 0; i < 6; i++) {
            ArrayList<TimeIntervalData> intervals =
                    data.getWeekDayTimeIntervalDataList(WeekDay.getByIndex(i));
            ScheduleItem[] schedule = new ScheduleItem[intervals.size()];
            for (int j = 0; j < intervals.size(); j++) {
                schedule[j] = intervals.get(j);
            }
            schedules.get(i).setValue(schedule);
        }

    }

    public LiveData<ScheduleItem[]> getSchedule(int index) {
        return schedules.get(index - 1);
    }
}
