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
        Schema::create('attendances', function (Blueprint $table) {
            $table->id();
            $table->foreignId('user_id')->constrained()->onDelete('cascade'); // Foreign key to users
            $table->foreignId('location_id')->constrained()->onDelete('cascade'); // Foreign key to locations
            $table->enum('type', ['hadir', 'izin', 'sakit', 'alpa'])->default('alpa'); // Add 'alpa' to the enum
            $table->enum('status', ['on time', 'late'])->default('on time'); // Default to 'on time'
            $table->text('reason')->nullable(); // Reason for izin/sakit
            $table->double('latitude'); // Location latitude
            $table->double('longitude'); // Location longitude
            $table->boolean('fake_gps')->default(false); // Fake GPS detection
            $table->timestamps();
        });
        
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('attendances');
    }
};
