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
        // Add the fake_gps_count column to the attendance_summaries table
        Schema::table('attendance_summaries', function (Blueprint $table) {
            $table->integer('fake_gps_count')->default(0); // Add fake GPS count column with default value of 0
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        // Drop the fake_gps_count column in case the migration is rolled back
        Schema::table('attendance_summaries', function (Blueprint $table) {
            $table->dropColumn('fake_gps_count');
        });
    }
};
