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
        Schema::create('attendance_summaries', function (Blueprint $table) {
            $table->id();
            $table->foreignId('user_id')->constrained()->onDelete('cascade'); // Foreign key to users
            $table->integer('month'); // Month (1-12)
            $table->integer('year'); // Year
            $table->integer('present_count')->default(0); // Number of hadir
            $table->integer('sick_count')->default(0); // Number of sakit
            $table->integer('leave_count')->default(0); // Number of izin
            $table->integer('late_count')->default(0); // Number of late
            $table->integer('alpa_count')->default(0); // Number of alpa
            $table->timestamps();

            // Ensure the user can only have one record per month and year
            $table->unique(['user_id', 'month', 'year']);
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('attendance_summary');
    }
};
