package io.bega.servicebase.screen.record;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.mikepenz.fastadapter_extensions.drag.ItemTouchCallback;
import com.mikepenz.fastadapter_extensions.drag.SimpleDragCallback;
import com.mikepenz.fastadapter_extensions.swipe.SimpleSwipeCallback;
import com.mikepenz.fastadapter_extensions.swipe.SimpleSwipeDragCallback;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.Bind;
import io.bega.servicebase.R;
import io.bega.servicebase.model.OrderTaskSwipeableItem;
import io.bega.servicebase.model.appointment.OrderTaskStatus;
import io.bega.servicebase.model.event.MenuMessageBusEvent;
import io.bega.servicebase.model.items.ExpandableItem;
import io.bega.servicebase.model.items.SwipeableItem;
import io.bega.servicebase.util.mortar.BaseView;
import mortar.Presenter;

public class RecordView extends BaseView implements ItemTouchCallback, SimpleSwipeCallback.ItemSwipeCallback {


    private SimpleDragCallback touchCallback;
    private ItemTouchHelper touchHelper;


    private Context context;

    @Inject
    Bus bus;

    @Inject
	RecordPresenter presenter;

	@Bind(R.id.view_record_rv)
	RecyclerView listView;

	private FastItemAdapter<IItem> fastItemAdapter;

	public RecordView(Context context, AttributeSet attrs) {
		super(context, attrs);
        this.context = context;
	}

	@Override
	protected Presenter getPresenter() {
		return presenter;
	}



	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		fastItemAdapter = new FastItemAdapter<>();



		//configure our fastAdapter
		fastItemAdapter.withOnClickListener(new FastAdapter.OnClickListener<IItem>() {
			@Override
			public boolean onClick(View v, IAdapter<IItem> adapter, IItem item, int position) {

                if (item instanceof OrderTaskSwipeableItem) {
                    String id = String.valueOf(((OrderTaskSwipeableItem)item).idtask);
                    presenter.moveToRecord(null);
                }
               // Toast.makeText(v.getContext(), (item).name.getText(v.getContext()), Toast.LENGTH_LONG).show();
				return false;
			}
		});

		//configure the itemAdapter
		fastItemAdapter.withFilterPredicate(new IItemAdapter.Predicate<IItem>() {
			@Override
			public boolean filter(IItem item, CharSequence constraint) {
				//return true if we should filter it out
				//return false to keep it
                SwipeableItem swipeableItem = null;
                if (item instanceof  SwipeableItem)
                {
                    swipeableItem = (SwipeableItem)item;
                }

				return !swipeableItem.name.getText().toLowerCase().contains(constraint.toString().toLowerCase());
			}
		});

		//get our recyclerView and do basic setup
        listView.setLayoutManager(new LinearLayoutManager(context));
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(fastItemAdapter);

		//fill with some sample data
		int x = 0;
		List<IItem> items = new ArrayList<>();
        String[] task_states = getResources().getStringArray(R.array.task_states);
        for (int z=0; z<task_states.length; z++) {
            if (task_states[z].startsWith("-")) continue;

            int count = new Random().nextInt(3);

            ExpandableItem expandableItem = new ExpandableItem().

                    withName(task_states[z] + " (" + String.valueOf(count) + ")").withIdentifier(z);
            List<IItem> subItems = new ArrayList<>();
            for (int i = 0; i < count; i++)
            {
                String id =  String.valueOf(new Random().nextInt(2500));
                subItems.add(i , new OrderTaskSwipeableItem(i, "AZ-" + id,  DateTime.now(),"Palau de plegamans", "AIDE", OrderTaskStatus.Concretado).withName(
                        "   " + "AZ-" + id).withIdentifier(i))
                ;
            }

            expandableItem.withSubItems(subItems);
            items.add(expandableItem);
        }


		fastItemAdapter.add(items);
        Drawable leaveBehindDrawableLeft = new IconicsDrawable(context)
                .icon(MaterialDesignIconic.Icon.gmi_delete)
                .color(Color.WHITE)
                .sizeDp(24);
        Drawable leaveBehindDrawableRight = new IconicsDrawable(context)
                .icon(MaterialDesignIconic.Icon.gmi_archive)
                .color(Color.WHITE)
                .sizeDp(24);

        touchCallback = new SimpleSwipeDragCallback(
                this,
                this,
                leaveBehindDrawableLeft,
                ItemTouchHelper.LEFT,
                ContextCompat.getColor(context, R.color.md_red_900)
        )
                .withBackgroundSwipeRight(ContextCompat.getColor(context, R.color.md_blue_900))
                .withLeaveBehindSwipeRight(leaveBehindDrawableRight);

        touchHelper = new ItemTouchHelper(touchCallback); // Create ItemTouchHelper and pass with parameter the SimpleDragCallback
        //touchHelper.attachToRecyclerView(listView); // Attach ItemTouchHelper to RecyclerView

        //restore selections (this has to be done after the items were added
        //fastItemAdapter.withSavedInstanceState(savedInstanceState);

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        bus.register(this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        bus.unregister(this);
    }

    @Subscribe
    public void subscribeMessage(MenuMessageBusEvent message)
    {
        if (message.getMessage() == 0xFF)
        {
            presenter.openCalendarDialog();
        }

    }

    @Override
    public boolean itemTouchOnMove(int oldPosition, int newPosition)
    {
        final IItem oldItem = fastItemAdapter.getItem(oldPosition);
        final IItem newItem = fastItemAdapter.getItem(newPosition);
        return false;
    }

    @Override
    public void itemSwiped(int position, int direction) {
        final IItem item = fastItemAdapter.getItem(position);
        if (item instanceof SwipeableItem)
        {
            final SwipeableItem swipeableItem = (SwipeableItem)item;
            swipeableItem.setSwipedDirection(direction);

            // This can vary depending on direction but remove & archive simulated here both results in
            // removal from list
            final Runnable removeRunnable = new Runnable() {
                @Override
                public void run() {
                    // item.setSwipedAction(null);

               /* int position = fastItemAdapter.getAdapterPosition(item);
                if (position != RecyclerView.NO_POSITION) {
                    fastItemAdapter.remove(position);
                }*/
                   // presenter.moveToRecord();

                }
            };
            final View rv = findViewById(R.id.view_record_rv);
            rv.postDelayed(removeRunnable, 3000);

            swipeableItem.setSwipedAction(new Runnable() {
                @Override
                public void run() {
                    rv.removeCallbacks(removeRunnable);
                    swipeableItem.setSwipedDirection(0);
                    int position = fastItemAdapter.getAdapterPosition(item);
                    if (position != RecyclerView.NO_POSITION) {
                        fastItemAdapter.notifyItemChanged(position);
                    }
                }
            });
        }

        fastItemAdapter.notifyItemChanged(position);
    }
}
