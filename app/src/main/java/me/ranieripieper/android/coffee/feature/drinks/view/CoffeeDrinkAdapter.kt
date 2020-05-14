package me.ranieripieper.android.coffee.feature.drinks.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_coffee.view.*
import me.ranieripieper.android.coffee.R
import me.ranieripieper.android.coffee.feature.drinks.data.CoffeeDrink
import kotlin.properties.Delegates

class CoffeeDrinkAdapter : RecyclerView.Adapter<CoffeeDrinkAdapter.CoffeeDrinkViewHolder>() {

    private var coffeeDrinkList: List<CoffeeDrink> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeDrinkViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_coffee, parent, false)
        return CoffeeDrinkViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = coffeeDrinkList.size

    override fun onBindViewHolder(holder: CoffeeDrinkViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val coffeeDrink: CoffeeDrink = coffeeDrinkList[position]
            holder.bind(coffeeDrink)
        }
    }

    // Update data
    fun updateData(newCoffeeDrinkList: List<CoffeeDrink>) {
        coffeeDrinkList = newCoffeeDrinkList
    }

    class CoffeeDrinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(coffeeDrink: CoffeeDrink) {
            itemView.tvCoffeeName.text = coffeeDrink.name

            Glide.with(itemView.context)
                .load(coffeeDrink.image)
                .centerCrop()
                .thumbnail()
                .into(itemView.ivCoffee)

            val actionDetail =
                CoffeeDrinkListFragmentDirections.showCoffeeDrinkDetail(
                    coffeeDrink.id
                )

            itemView.setOnClickListener {
                Navigation.findNavController(itemView).navigate(actionDetail)
            }
        }
    }
}