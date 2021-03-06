package rus.voiceassistant.view.notification;

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v7.app.NotificationCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.notifications_fragment.*
import rus.voiceassistant.*
import rus.voiceassistant.model.actions.Notification
import rus.voiceassistant.presenter.ItemPresenter
import rus.voiceassistant.presenter.notification.NotificationPresenter
import rus.voiceassistant.receivers.NotificationReceiver
import rus.voiceassistant.view.notification.CreateNotificationFragmentDialog
import rus.voiceassistant.view.ItemCreationListener
import rus.voiceassistant.view.ItemView
import rus.voiceassistant.view.adapters.NotificationsAdapter
import java.util.*

/**
 * Created by RUS on 28.04.2016.
 */
class NotificationsFragment : Fragment(), ItemView<Notification>, ItemCreationListener<Notification>, NotificationsAdapter.OnItemClickListener {

    val presenter: ItemPresenter<Notification> = NotificationPresenter(this)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.notifications_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        fab.setOnClickListener { presenter.onAddActionClicked() }

        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onActionAdded() = recyclerView.adapter.notifyItemInserted(recyclerView.adapter.itemCount - 1)

    override fun onActionRemoved(position: Int) = recyclerView.adapter.notifyItemRemoved(position)

    override fun setActions(actions: ArrayList<Notification>) {
        recyclerView.adapter = NotificationsAdapter(this, actions);
    }

    override fun onItemClicked(position: Int) = presenter.onActionClicked(position)

    override fun onLongItemClicked(position: Int): Boolean {
        presenter.onLongActionClicked(position)
        return true
    }

    override fun showItemFragmentDialog(notification: Notification?) {
        val fragmentManager = (activity as MainActivity).supportFragmentManager
        var createNotificationDialog: CreateNotificationFragmentDialog
        if(notification == null)
            createNotificationDialog = CreateNotificationFragmentDialog.newInstance(mode = CreateNotificationFragmentDialog.MODE_CREATE)
        else createNotificationDialog = CreateNotificationFragmentDialog.newInstance(notification, CreateNotificationFragmentDialog.MODE_EDIT)

        createNotificationDialog.setTargetFragment(this, 300)
        createNotificationDialog.show(fragmentManager, "fragment_create_notification");
    }

    override fun showDeleteItemFragmentDialog(position: Int) {
        MaterialDialog.Builder(activity)
                .content(R.string.deleteNotificationTitle)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive { materialDialog: MaterialDialog, dialogAction: DialogAction -> presenter.removeAction(position)}
                .show()
    }

    override fun onItemCreated(notification: Notification) = presenter.onItemCreated(notification)

    override fun onItemEdited(notification: Notification) = presenter.onItemEdited(notification)

    override fun onDataSetChanged() = recyclerView.adapter.notifyDataSetChanged()

    override fun showSnackBar(text: String) = toast(text)

    override fun getContext(): Context = activity

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

}
