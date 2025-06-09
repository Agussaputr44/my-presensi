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
            $table->string('device_name')->nullable()->after('check_out_time'); // Kolom nama perangkat
            $table->string('attachment')->nullable()->after('device_name'); // Kolom lampiran
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('attendances', function (Blueprint $table) {
            $table->dropColumn('device_name');
            $table->dropColumn('attachment');
        });
    }
};