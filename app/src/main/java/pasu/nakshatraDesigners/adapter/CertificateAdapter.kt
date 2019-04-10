package pasu.nakshatraDesigners.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.navigation.Navigation
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.VideoListActivity
import pasu.nakshatraDesigners.data.VideoListItem
import pasu.nakshatraDesigners.fragments.CertificatesDirections
import pasu.nakshatraDesigners.utils.NetworkState
import pasu.nakshatraDesigners.utils.VIDEO_URL
import pasu.nakshatraDesigners.utils.widgets.CustomTextview


class CertificateAdapter internal constructor(val context: Context) :
    PagedListAdapter<VideoListItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
    private var networkState: NetworkState? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        return when (viewType) {
            R.layout.certificate_list_item -> {
                view = layoutInflater.inflate(R.layout.certificate_list_item, parent, false)
                PostItemViewHolder(
                    pasu.nakshatraDesigners.databinding.CertificateListItemBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            R.layout.network_state_item -> {
                view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
                NetworkStateItemViewHolder(view)
            }
            R.layout.headerlayout -> {
                view = layoutInflater.inflate(R.layout.headerlayout, parent, false)
                HeaderViewHolder(
                    pasu.nakshatraDesigners.databinding.HeaderlayoutBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            else -> throw IllegalArgumentException("unknown type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.certificate_list_item -> {
//                (holder as PostItemViewHolder).bindTo(getItem(position))
                val item = getItem(position)
                item?.run {
                    (holder as PostItemViewHolder).apply {
                        bind(this@run)
                        itemView.tag = this@run
                        rootLayout.findViewById<View>(R.id.ViewImage).setOnClickListener {
                            val action = CertificatesDirections.actionCertificatesToCertificateDetailFrag(
                                getItem(position)?.aari_video_url ?: ""
                            )
                            val intent= Intent(context,VideoListActivity::class.java)
                            intent.apply {
                                putExtra(VIDEO_URL,getItem(position)?.getVideoUrl(context))
                                context.startActivity(intent)
                            }

//                            Navigation.findNavController(context as Activity, R.id.nav_host_frag).navigate(action)
                        }
                    }
                }
            }
            R.layout.network_state_item -> (holder as NetworkStateItemViewHolder).bindView(networkState)

            R.layout.headerlayout -> {
//                (holder as PostItemViewHolder).bindTo(getItem(position))
                val item = getItem(position)
                item?.run {
                    (holder as HeaderViewHolder).apply {
                        bind(this@run)
                        itemView.tag = this@run

                    }
                }
            }
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState !== NetworkState.LOADED && networkState !== NetworkState.MAXPAGE
    }

    override fun getItemViewType(position: Int): Int {
        println("getItemViewType" + "__________" + hasExtraRow() + "_________" + position + "_____" + itemCount)
        if (hasExtraRow() && position == itemCount - 1) {
            println("getItemViewType")
            return R.layout.network_state_item
        } else {
            println("getItemViewType1")
            if (getItem(position)?.listType == 0)
                return R.layout.certificate_list_item
            else
                return R.layout.headerlayout
        }
    }

    internal fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val previousExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val newExtraRow = hasExtraRow()
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (newExtraRow && previousState !== newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }


    class PostItemViewHolder(
        private val binding: pasu.nakshatraDesigners.databinding.CertificateListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        var rootLayout = binding.root

        fun bind(item: VideoListItem) {
            binding.apply {
                listItem = item
                executePendingBindings()
            }


        }

//        rootLayout.set
    }


    class HeaderViewHolder(
        private val binding: pasu.nakshatraDesigners.databinding.HeaderlayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        var rootLayout = binding.root

        fun bind(item: VideoListItem) {
            binding.apply {
                listItem = item
                executePendingBindings()
            }


        }

//        rootLayout.set
    }

    internal class NetworkStateItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val progressBar: ProgressBar
        private val errorMsg: CustomTextview
        private val button: Button

        init {
            progressBar = itemView.findViewById(R.id.progress_bar)
            errorMsg = itemView.findViewById(R.id.error_msg)
            button = itemView.findViewById(R.id.retry_button)
            button.setOnClickListener { }
        }

        fun bindView(networkState: NetworkState?) {
            println("networkstate---" + networkState!!.status)
            if (networkState != null && networkState.status == NetworkState.Status.RUNNING) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
            if (networkState != null && networkState.status == NetworkState.Status.FAILED) {
                errorMsg.visibility = View.VISIBLE
                errorMsg.text = networkState.msg
            } else if (networkState != null && networkState.status == NetworkState.Status.MAX) {
                errorMsg.visibility = View.VISIBLE
                errorMsg.text = "No More Page to Load"
            } else {
                errorMsg.visibility = View.GONE
            }
        }


    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VideoListItem>() {
            override fun areItemsTheSame(oldItem: VideoListItem, newItem: VideoListItem): Boolean {
                return false
            }

            override fun areContentsTheSame(oldItem: VideoListItem, newItem: VideoListItem): Boolean {
                return false
            }
        }
    }

}