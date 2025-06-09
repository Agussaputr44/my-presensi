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
        Schema::table('attendances', function (Blueprint $table) {
            $table->time('check_in_time')->nullable(); // Jam masuk
            $table->time('check_out_time')->nullable(); // Jam keluar
            $table->dropColumn('fake_gps'); // Hapus kolom fake_gps
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('attendances', function (Blueprint $table) {
            $table->dropColumn(['check_in_time', 'check_out_time']); 
            $table->boolean('fake_gps')->default(false); 
        });
    }
};
