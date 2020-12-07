package ru.samsung.itshool.memandos.ui.Fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.samsung.itshool.memandos.R
import ru.samsung.itshool.memandos.di.ComponentHolder
import ru.samsung.itshool.memandos.domain.Mem
import ru.samsung.itshool.memandos.ui.Activites.DetailMemActivity
import ru.samsung.itshool.memandos.ui.VM.RobbionVM
import ru.samsung.itshool.memandos.ui.adapters.MemsAdapter
import ru.samsung.itshool.memandos.utils.SnackBarsUtil
import java.util.*

private const val TAG = "RibbonFragment"

class RibbonFragment : Fragment(), MemsAdapter.AdapterInteractionListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var textFailure: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var memsAdapter: MemsAdapter
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager

    private lateinit var ribbonToolbar: Toolbar

    private lateinit var robbionVM: RobbionVM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "create ribbon fragment")
        retainInstance = true

        setHasOptionsMenu(true)
        robbionVM = ViewModelProvider(this).get(RobbionVM::class.java)
        ComponentHolder.appComponent.inject(robbionVM)
    }

    fun bindingView(v: View) {
        ribbonToolbar = v.findViewById(R.id.main_toolbar)

        with((activity as AppCompatActivity)) {
            this.setSupportActionBar(ribbonToolbar)
        }
        swipeContainer = v.findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            robbionVM.refreshMemes().observe(viewLifecycleOwner, Observer { diffResult ->
                when {
                    diffResult.isSuccess -> {
                        swipeContainer.isRefreshing = false
                        diffResult.getOrNull()?.dispatchUpdatesTo(memsAdapter)
                    }
                    diffResult.isFailure -> {
                        failureLoad(v)
                    }
                }
            }
            )
        }

        staggeredGridLayoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        textFailure = v.findViewById(R.id.text_failure)
        progressBar = v.findViewById(R.id.progressBar)
        recyclerView = v.findViewById(R.id.recycler_view_mems)
    }

    fun initVM(v: View) {

        robbionVM.getMemes().observe(viewLifecycleOwner, Observer<Result<Collection<Mem>>> {
            when {
                it.isSuccess -> {
                    val memes = it.getOrDefault(LinkedList()).toMutableList()
                    successLoadMemes(v, memes)
                }
                it.isFailure -> {
                    failureLoadMemes()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_ribbon, container, false)

        bindingView(v)
        initVM(v)

        return v
    }


    fun successLoadMemes(v: View, memes: List<Mem>) {
        recyclerView.layoutManager = staggeredGridLayoutManager
        val memsAdapter2 = MemsAdapter(memes.toTypedArray(), this)
        Log.d(TAG, "i : " + memes.size)
        Log.d(TAG, " " + memes)
        recyclerView.adapter = memsAdapter2
        swipeContainer.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        Log.d(TAG, "success")
    }

    fun failureLoadMemes() {
        progressBar.visibility = View.GONE
        textFailure.visibility = View.VISIBLE
        Log.d(TAG, "failure")
    }


    fun failureLoad(v: View) {
        swipeContainer.isRefreshing = false
        SnackBarsUtil.errorSnackBar(R.string.failureRefresh.toString(), v)
    }

    companion object {
        @JvmStatic
        fun newInstance() = RibbonFragment()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        Log.d(TAG, "optins menu ribbion")
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onItemClick(mem: Mem) {
        context?.let {
            val intent = DetailMemActivity.getIntent(it, mem)
            startActivity(intent)
        }

    }
}
