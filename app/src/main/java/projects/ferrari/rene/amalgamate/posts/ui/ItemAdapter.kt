package projects.ferrari.rene.amalgamate.posts.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import projects.ferrari.rene.amalgamate.posts.ui.filtergroup.GroupType
import projects.ferrari.rene.amalgamate.R
import projects.ferrari.rene.amalgamate.posts.api.FetchedItem
import projects.ferrari.rene.amalgamate.core.extensions.color
import projects.ferrari.rene.amalgamate.core.extensions.drawable

class ItemAdapter(
    private val context: Context,
    private val groupType: GroupType,
    private var items: List<FetchedItem> = emptyList(),
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(layoutInflater.inflate(R.layout.vh_item, parent, false))
    }

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = holder.bind(items[position])

    fun updateItems(newItems: List<FetchedItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvVotes = itemView.findViewById<TextView>(R.id.tvVotes)
        private val tvOrigin = itemView.findViewById<TextView>(R.id.tvOrigin)
        private val ivArrowUp = itemView.findViewById<ImageView>(R.id.iv_arrow_up)
        private val flContainer = itemView.findViewById<FrameLayout>(R.id.flContainer)

        fun bind(item: FetchedItem) {
            tvTitle.text = item.title
            tvVotes.text = item.votes.toString()
            tvOrigin.background.setTint(
                ContextCompat.getColor(context, item.origin.color)
            )
            tvOrigin.text = item.origin.title
            itemView.setOnClickListener {
                onItemClick(item.urlStr)
            }

            //setting correct colors depending on the shown group
            flContainer.foreground = context.drawable(groupType.rippleDrawable)
            tvVotes.setTextColor(context.color(groupType.accentColor))
            DrawableCompat.setTint(ivArrowUp.drawable, context.color(groupType.accentColor))
        }
    }
}
