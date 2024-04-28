package com.glucode.about_you.engineers

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.glucode.about_you.R
import com.glucode.about_you.databinding.FragmentEngineersBinding
import com.glucode.about_you.engineers.models.Engineer
import com.glucode.about_you.mockdata.MockData
import com.glucode.about_you.mockdata.MockData.engineers
import java.time.Year
import java.util.Calendar


data class QuickStats (val joinYear: Int,val coffee: Int, val bugCount: Int)
data class Engineer(val name: String, val quickStats: QuickStats)
class EngineersFragment : Fragment() {
    private lateinit var binding: FragmentEngineersBinding

    private val mockEngineers = listOf(
        Engineer("Reenen", QuickStats(2015,5400,1000)),
        Engineer("Wilmar", QuickStats(2006,4000,4000)),
        Engineer("Eben", QuickStats(2007,1000,100)),
        Engineer("Stefan", QuickStats(2014,9000,700)),
        Engineer("Brandon", QuickStats(2012,99999,99999)),
        Engineer("Henri", QuickStats(2011,1800,1000))
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEngineersBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        setUpEngineersList(MockData.engineers)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_engineers, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_years) {
            filterEngineersByYears()
            return true
        }
        if (item.itemId == R.id.action_coffees){
            filterEngineersByCoffeeDrinks()
            return true
        }
        if (item.itemId == R.id.action_bugs){
            filterEngineersByBugs()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun calculateYearsOfExperience(engineer: Engineer): Int {
        val currentYear = Year.now().value
        return currentYear - engineer.quickStats.years
    }

    fun calculateTotalBugs(): Int {
        return engineers.sumBy { engineer ->
            engineer.quickStats.bugs
        }
    }
    internal fun filterEngineersByYears() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val filteredEngineers = engineers.filter { engineer ->
            calculateYearsOfExperience(engineer) >= 5
        }.sortedBy { engineer ->
            calculateYearsOfExperience(engineer)
        }
        setUpEngineersList(filteredEngineers)
    }

    internal fun filterEngineersByCoffeeDrinks(){
        val filteredEngineers = engineers.filter { engineer ->
            engineer.quickStats.coffees > 0
        }.sortedBy { engineer ->
            engineer.quickStats.coffees
        }
        setUpEngineersList(filteredEngineers)
    }
    internal fun filterEngineersByBugs(){
        val filteredEngineers = engineers.filter { engineer ->
            engineer.quickStats.bugs > 0
        }.sortedBy { engineer ->
            engineer.quickStats.bugs
        }
        setUpEngineersList(filteredEngineers)
    }

    private fun setUpEngineersList(engineers: List<Engineer>) {
        binding.list.adapter = EngineersRecyclerViewAdapter(engineers) {
            goToAbout(it)
        }
        val dividerItemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(dividerItemDecoration)
    }

    private fun goToAbout(engineer: Engineer) {
        val bundle = Bundle().apply {
            putString("name", engineer.name)
        }
        findNavController().navigate(R.id.action_engineersFragment_to_aboutFragment, bundle)
    }
}