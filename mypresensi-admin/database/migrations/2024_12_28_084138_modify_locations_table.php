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
        Schema::table('locations', function (Blueprint $table) {
            // Add new columns
            $table->string('alamat', 255)->after('name');  // Office address
            $table->text('keterangan')->after('alamat');  // Description of the office

            // Drop old columns
            $table->dropColumn(['latitude', 'longitude', 'radius']);
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('locations', function (Blueprint $table) {
            // Drop new columns
            $table->dropColumn(['alamat', 'keterangan']);

            // Add back old columns
            $table->double('latitude', 15, 8)->after('name');  // Latitude coordinate
            $table->double('longitude', 15, 8)->after('latitude');  // Longitude coordinate
            $table->float('radius')->after('longitude');  // Valid radius in meters
        });
    }
};
