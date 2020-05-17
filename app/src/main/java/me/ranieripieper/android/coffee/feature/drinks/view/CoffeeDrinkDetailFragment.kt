package me.ranieripieper.android.coffee.feature.drinks.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_coffee_drink_detail.*
import kotlinx.android.synthetic.main.item_coffee.ivCoffee
import kotlinx.android.synthetic.main.item_coffee.tvCoffeeName
import me.ranieripieper.android.coffee.R
import me.ranieripieper.android.coffee.core.view.BaseFragment
import me.ranieripieper.android.coffee.core.viewmodel.ViewState
import me.ranieripieper.android.coffee.feature.drinks.viewmodel.CoffeeDrinkViewModel

class CoffeeDrinkDetailFragment : BaseFragment<CoffeeDrinkViewModel>(true) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_coffee_drink_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        val id = CoffeeDrinkDetailFragmentArgs.fromBundle(
            requireArguments()
        ).coffeeDrinkId

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            when (viewState) {
                is ViewState.Error -> {
                    Toast.makeText(context, viewState.error, Toast.LENGTH_SHORT).show()
                    view?.let { Navigation.findNavController(it).popBackStack() }
                }
                is ViewState.Loading ->
                    loadingView.visibility = if (viewState.loading) View.VISIBLE else View.GONE
            }
        })

        viewModel.coffeeDrinkDetail.observe(viewLifecycleOwner, Observer { coffeeDrink ->
            tvCoffeeName.text = coffeeDrink.name
            context?.let {
                Glide.with(it)
                    .load(coffeeDrink.image)
                    .centerCrop()
                    .thumbnail()
                    .into(ivCoffee)
            }
            tvCoffeeCupValue.text = coffeeDrink.cup
            tvCoffeeRatioValue.text = coffeeDrink.ratio
        })

        viewModel.fetchCoffeeDrink(id)
    }

}