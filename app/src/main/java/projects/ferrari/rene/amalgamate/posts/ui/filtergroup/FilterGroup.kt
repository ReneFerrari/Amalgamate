package projects.ferrari.rene.amalgamate.posts.ui.filtergroup

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import projects.ferrari.rene.amalgamate.*
import projects.ferrari.rene.amalgamate.core.AppThemer
import projects.ferrari.rene.amalgamate.core.Constants

class FilterGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {

    private val llContainer = View.inflate(
        context,
        R.layout.filter_group_container, null
    ) as LinearLayout

    private val viewIds = IntArray(TimeFrame.values().size) {
        View.generateViewId()
    }
    private val androidGroup = View.inflate(
        context,
        R.layout.filter_group_android, null
    ) as MaterialButtonToggleGroup

    private val iosGroup = View.inflate(
        context, R.layout.filter_group_ios, null
    ) as MaterialButtonToggleGroup

    /*
    Since it's not possible to change the Theme of the MaterialGroup at runtime, a separate group
    themed with Android and iOS exist.
     */
    private val groups = mapOf(
        GroupType.ANDROID to androidGroup,
        GroupType.IOS to iosGroup
    )
    private val defaultTimeFrameName = TimeFrame.DAY.name

    private var currentShownGroup =
        GroupType.ANDROID
    private var currentSelectedTimeFrame: TimeFrame? = null

    init {
        isHorizontalScrollBarEnabled = false
        isVerticalScrollBarEnabled = false

        groups.forEach { group ->
            addFilterButtons(group.value)
        }
        llContainer.addView(groups[GroupType.ANDROID])
        addView(llContainer)

        llContainer.findViewById<ImageView>(R.id.ivModeSwitch).setOnClickListener {
            AppThemer.inverseTheme(context.applicationContext)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setCorrectButtonChecked()
    }

    override fun onSaveInstanceState() = Bundle().apply {
        putParcelable(Constants.KEY_SUPER_STATE, super.onSaveInstanceState())
        putString(
            Constants.KEY_TIME_FRAME,
            currentSelectedTimeFrame?.name ?: defaultTimeFrameName
        )
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var superState = state
        if (state is Bundle) {
            currentSelectedTimeFrame = TimeFrame.valueOf(
                state.getString(Constants.KEY_TIME_FRAME) ?: defaultTimeFrameName
            )
            superState = state.getParcelable(Constants.KEY_SUPER_STATE)
        }
        super.onRestoreInstanceState(superState)
    }

    /*
    Adding MaterialButtons for each TimeFrame to the parent
    */
    private fun addFilterButtons(parent: MaterialButtonToggleGroup) {
        TimeFrame.values().forEachIndexed { index, timeFrame ->
            parent.addView((View.inflate(
                parent.context,
                R.layout.filter_button, null
            ) as MaterialButton).apply {
                tag = timeFrame.name
                text = context.getString(timeFrame.nameId)
                id = viewIds[index]
            })
        }
    }

    private fun setCorrectButtonChecked() {
        val idToCheck = viewIds[
                TimeFrame.values().indexOfFirst { timeFrame ->
                    timeFrame == currentSelectedTimeFrame ?: TimeFrame.DAY
                }
        ]
        groups[currentShownGroup]?.check(idToCheck)
    }

    fun showGroup(groupType: GroupType) {
        if (currentShownGroup == groupType) {
            return
        }
        currentShownGroup = groupType

        //Just removing old and adding current group
        when (groupType) {
            GroupType.IOS -> {
                llContainer.removeView(androidGroup)
                iosGroup.check(androidGroup.checkedButtonId)
                llContainer.addView(iosGroup)
            }
            GroupType.ANDROID -> {
                llContainer.removeView(iosGroup)
                androidGroup.check(iosGroup.checkedButtonId)
                llContainer.addView(androidGroup)
            }
        }
    }

    fun onCheckChanged(callback: (TimeFrame) -> Unit) {
        groups.values.forEach { group ->
            group.addOnButtonCheckedListener { checkedGroup, checkedId, isChecked ->
                if (isChecked) {
                    val newTimeFrame = TimeFrame.valueOf(
                        checkedGroup.findViewById<MaterialButton>(checkedId).tag.toString()
                    )

                    currentSelectedTimeFrame = newTimeFrame
                    callback(newTimeFrame)
                }
            }
        }
    }
}
