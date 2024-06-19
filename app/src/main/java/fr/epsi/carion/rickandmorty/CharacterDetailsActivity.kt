package fr.epsi.carion.rickandmorty

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import fr.epsi.carion.rickandmorty.network.RickAndMortyApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDetailsActivity : AppCompatActivity() {

    private lateinit var toolbarTitleTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_details)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Hide default title

        toolbarTitleTextView = findViewById(R.id.toolbarTitle)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val characterId = intent.getIntExtra("character_id", -1)
        if (characterId != -1) {
            fetchCharacterDetails(characterId)
        } else {
            showError("ID de personnage invalide.")
        }
    }

    private fun fetchCharacterDetails(characterId: Int) {
        RickAndMortyApiService.api.getCharacter(characterId).enqueue(object : Callback<Character> {
            override fun onResponse(call: Call<Character>, response: Response<Character>) {
                if (response.isSuccessful) {
                    val character = response.body()
                    if (character != null) {
                        updateUI(character)
                    } else {
                        showError("Impossible de récupérer les détails du personnage.")
                    }
                } else {
                    showError("Impossible de récupérer les détails du personnage.")
                }
            }

            override fun onFailure(call: Call<Character>, t: Throwable) {
                showError("Impossible de récupérer les détails du personnage.")
            }
        })
    }

    private fun updateUI(character: Character) {
        toolbarTitleTextView.text = character.name // Set character name in the Toolbar
        findViewById<TextView>(R.id.characterSpecies).text = character.species
        findViewById<TextView>(R.id.characterStatus).text = character.status
        findViewById<TextView>(R.id.characterGender).text = character.gender
        findViewById<TextView>(R.id.characterOrigin).text = character.origin.name
        findViewById<TextView>(R.id.characterLocation).text = character.location.name

        val characterImageView = findViewById<ImageView>(R.id.characterImage)
        characterImageView.load(character.image) {
            // Add image loading options if necessary
        }
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}
