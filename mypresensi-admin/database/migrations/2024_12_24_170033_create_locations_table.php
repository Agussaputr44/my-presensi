<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('locations', function (Blueprint $table) {
            $table->id();  // Primary key, auto increment
            $table->string('name', 100);  // Name of the location
            $table->double('latitude', 15, 8);  // Latitude coordinate
            $table->double('longitude', 15, 8);  // Longitude coordinate
            $table->float('radius');  // Valid radius in meters
            $table->timestamps();  // created_at and updated_at timestamps
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('locations');
    }
};
