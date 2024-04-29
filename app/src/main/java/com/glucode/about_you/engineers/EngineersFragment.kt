package com.glucode.about_you.engineers

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.glucode.about_you.R
import com.glucode.about_you.databinding.FragmentEngineersBinding
import com.glucode.about_you.engineers.models.Engineer
import com.glucode.about_you.mockdata.MockData
import com.glucode.about_you.mockdata.MockData.engineers
import com.squareup.picasso.Picasso
import java.time.Year
import java.util.Calendar


data class QuickStats (val joinYear: Int,val coffee: Int, val bugCount: Int)
data class Engineer(val name: String, val quickStats: QuickStats)
class EngineersFragment : Fragment()  {
    private lateinit var binding: FragmentEngineersBinding
    private val REQUEST_CODE_GALLERY = 2001
    private lateinit var adapter: EngineersRecyclerViewAdapter
    private lateinit var profileImage: ImageView

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
        val view = inflater.inflate(R.layout.item_engineer, container, false)


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

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            profileImage.setImageURI(selectedImageUri)

        }
        }
    }


