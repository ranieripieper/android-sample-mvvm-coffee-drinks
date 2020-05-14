package me.ranieripieper.android.coffee.feature.drinks.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_drinks_list.*
import me.ranieripieper.android.coffee.R
import me.ranieripieper.android.coffee.core.view.BaseFragment
import me.ranieripieper.android.coffee.core.viewmodel.ViewState
import me.ranieripieper.android.coffee.feature.drinks.viewmodel.CoffeeDrinkViewModel

class CoffeeDrinkListFragment : BaseFragment<CoffeeDrinkViewModel>(true) {

    private lateinit var coffeeDrinkAdapter: CoffeeDrinkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_drinks_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        coffeeDrinkAdapter =
            CoffeeDrinkAdapter()

        rvCoffeeDrinks.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = coffeeDrinkAdapter
        }

        initViewModel()
    }

    private fun initViewModel() {

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            when (viewState) {
                is ViewState.Error -> {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage(viewState.error)
                        .setPositiveButton(R.string.close
                        ) { dialog, _ ->
                            dialog.dismiss()
                        }
                    builder.create().show()
                }

                is ViewState.Loading ->
                    loadingView.visibility = if (viewState.loading) View.VISIBLE else View.GONE
            }
        })

        viewModel.coffeeDrinkList.observe(viewLifecycleOwner, Observer { coffeeDrinkList ->
            coffeeDrinkAdapter.updateData(coffeeDrinkList!!)
        })
    }

}