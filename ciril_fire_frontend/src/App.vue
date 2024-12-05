<template>
  <div id="app" class="flex flex-col items-center justify-center min-h-screen bg-gray-100">
    <h1 class="text-4xl font-bold mb-4">Dynamic Grid</h1>

    <button
      @click="incrementGrid"
      class="px-6 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 mb-4"
    >
      Run
    </button>

    <div v-if="grid.length" class="grid gap-2" :style="{ gridTemplateColumns: `repeat(${cols}, 1fr)` }">
      <div
        v-for="(cell, index) in flattenedGrid"
        :key="index"
        class="flex items-center justify-center w-12 h-12 border bg-gray-200 text-xl font-bold"
      >
        {{ cell }}
      </div>
    </div>

    <button
      @click="resetGrid"
      class="px-6 py-2 bg-red-500 text-white rounded hover:bg-red-600 mt-4"
    >
      Reset
    </button>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "App",
  data() {
    return {
      grid: [],
      rows: 0,
      cols: 0,
    };
  },
  computed: {
    flattenedGrid() {
      return this.grid.flat();
    },
  },
  methods: {
    async fetchGrid() {
      try {
        const response = await axios.get("http://localhost:8080/api/runner/grid");
        this.grid = response.data;
        this.rows = response.data.length;
        this.cols = response.data[0].length;
      } catch (error) {
        console.error("Error fetching grid:", error);
      }
    },
    async incrementGrid() {
      try {
        const response = await axios.post("http://localhost:8080/api/runner/increment");
        this.grid = response.data;
      } catch (error) {
        console.error("Error incrementing grid:", error);
      }
    },
    async resetGrid() {
      try {
        const response = await axios.post("http://localhost:8080/api/runner/reset");
        this.grid = response.data;
      } catch (error) {
        console.error("Error resetting grid:", error);
      }
    },
  },
  mounted() {
    this.fetchGrid();
  },
};
</script>

<style>
.grid {
  display: grid;
  gap: 10px;
}
</style>
