<template>
  <div id="app" class="flex flex-col items-center justify-center min-h-screen bg-gray-100">
    <h1 class="text-4xl font-bold mb-4">Counter: {{ counter }}</h1>
    <button
      @click="incrementCounter"
      class="px-6 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
    >
      Run
    </button>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'App',
  data() {
    return {
      counter: 0, // Compteur initial
    };
  },
  methods: {
    async fetchCounter() {
      try {
        const response = await axios.get('http://localhost:8080/api/counter');
        this.counter = response.data;
      } catch (error) {
        console.error('Error fetching counter:', error);
      }
    },
    async incrementCounter() {
      try {
        const response = await axios.post('http://localhost:8080/api/counter/increment');
        this.counter = response.data;
      } catch (error) {
        console.error('Error incrementing counter:', error);
      }
    },
  },
  mounted() {
    this.fetchCounter(); // Récupère la valeur initiale du compteur au chargement
  },
};
</script>
