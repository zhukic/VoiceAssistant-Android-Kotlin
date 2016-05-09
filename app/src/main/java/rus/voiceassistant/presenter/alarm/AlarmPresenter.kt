package rus.voiceassistant.presenter.alarm

import android.util.Log
import rus.voiceassistant.Logger
import rus.voiceassistant.MyApplication
import rus.voiceassistant.database.DatabaseManager
import rus.voiceassistant.model.Alarm
import rus.voiceassistant.view.alarm.IAlarmView
import java.util.*

/**
 * Created by RUS on 12.04.2016.
 */
class AlarmPresenter(var view: IAlarmView?) : IAlarmPresenter {

    val alarms: ArrayList<Alarm> = DatabaseManager.getAlarmsListFromDatabase()

    override fun onCheckedChanged(position: Int, isChecked: Boolean) {
        Logger.log(position.toString() + " " + isChecked)
        /*if(isChecked == true)
            view?.onAlarmOn(alarms.get(position))
        else
            view?.onAlarmOff(alarms.get(position))*/
    }

    override fun onResume() {
        view?.setActions(alarms)
    }

    override fun onAddActionClicked() {
        view?.showTimePicker()
    }

    override fun onActionClicked(position: Int) {
    }

    override fun onLongActionClicked(position: Int) {
    }

    override fun removeAction(position: Int) {
        MyApplication.alarmDao.deleteById(alarms[position].id)
        alarms.removeAt(position)

    }

    override fun createAlarm(hourOfDay: Int, minute: Int) {
        val alarm = Alarm()
        alarm.hour = hourOfDay
        alarm.minute = minute
        alarms.add(alarm)
        MyApplication.alarmDao.create(alarm)
        view?.onActionAdded()
    }

    override fun onDestroy() {
        view = null
    }

}