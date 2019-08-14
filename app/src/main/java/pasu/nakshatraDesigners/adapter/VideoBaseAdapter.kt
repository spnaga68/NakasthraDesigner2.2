package pasu.nakshatraDesigners.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pasu.nakshatraDesigners.R
import pasu.nakshatraDesigners.VideoListActivity
import pasu.nakshatraDesigners.data.VideoListItem
import pasu.nakshatraDesigners.databinding.CertificateListItemBinding
import pasu.nakshatraDesigners.fragments.CertificatesDirections
import pasu.nakshatraDesigners.utils.VIDEO_URL


class VideoBaseAdapter(val context: Context, val myDataSet: ArrayList<VideoListItem>) :
    RecyclerView.Adapter<VideoBaseAdapter.PostItemViewHolder>() {
    // Create new views (invoked by the layout manager)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.certificate_list_item, parent, false)

        return PostItemViewHolder(
            CertificateListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }



//
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: VideoBaseAdapter.PostItemViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

    val item = myDataSet.get(position)
    item?.run {
        (holder as VideoBaseAdapter.PostItemViewHolder).apply {
            bind(this@run)
            itemView.tag = this@run
            rootLayout.findViewById<View>(R.id.ViewImage).setOnClickListener {

                val intent = Intent(context, VideoListActivity::class.java)
                intent.apply {
                    putExtra(VIDEO_URL, myDataSet.get(position)?.getVideoUrl(context))
                    context.startActivity(intent)
                }

//                            Navigation.findNavController(context as Activity, R.id.nav_host_frag).navigate(action)
            }
        }
    }


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataSet.size

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









}