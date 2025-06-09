<?php
namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Carbon\Carbon;
use Illuminate\Support\Facades\Storage;

class Attendance extends Model
{
    protected $fillable = [
        'user_id',
        'location_id',
        'type',
        'status',
        'reason',
        'latitude',
        'longitude',
        'check_in_time',
        'check_out_time',
        'fake_gps',
        'location_name',
        'device_name',
        'attachment', // Tambahkan kolom attachment
    ];

    public function setCheckInTimeAttribute($value)
    {
        $this->attributes['check_in_time'] = $value;

        $checkInTime = Carbon::parse($value);
        $expectedTime = Carbon::parse('08:00:00'); // 8 AM

        // Set status based on check-in time
        $this->attributes['status'] = $checkInTime->gt($expectedTime) ? 'late' : 'on time';
    }

    public function user()
    {
        return $this->belongsTo(User::class);
    }

    public function location()
    {
        return $this->belongsTo(Location::class);
    }

    /**
     * Method untuk melacak absensi user.
     */
    public static function trackUserAttendance($userId)
    {
        $today = Carbon::today();
        $startOfWeek = $today->copy()->startOfWeek(Carbon::MONDAY);
        $endOfWeek = $startOfWeek->copy()->addDays(5); 
        $user = User::find($userId);
        $defaultLocationId = $user->location_id ?? 1;

        for ($date = $startOfWeek->copy(); $date <= $endOfWeek; $date->addDay()) {
            if ($date->isAfter($today)) {
                continue;
            }

            $attendance = self::where('user_id', $userId)
                ->whereDate('created_at', $date)
                ->first();

            if (!$attendance) {
                self::create([
                    'user_id' => $userId,
                    'location_id' => $defaultLocationId,
                    'type' => 'alpa',
                    'latitude' => '0',
                    'longitude' => '0',
                    'check_in_time' => null,
                    'check_out_time' => null,
                    'created_at' => $date->format('Y-m-d 00:00:00'),
                    'updated_at' => now(),
                ]);
            } else {
                // Check if today is already filled
                if ($date->isSameDay($today) && $attendance->type !== 'alpa') {
                    continue;
                }
            }
        }
    }

    /**
     * Boot method untuk validasi dan pengaturan jam masuk/keluar.
     */
    protected static function booted()
    {
        static::saving(function ($attendance) {
            // Validasi tipe absensi
            if (in_array($attendance->type, ['sakit', 'izin']) && !$attendance->reason) {
                throw new \Exception("Reason is required for 'sakit' or 'izin'.");
            }

            if ($attendance->type === 'hadir') {
                // Validasi jam masuk
                if (!$attendance->check_in_time) {
                    $attendance->check_in_time = now(); // Default ke waktu saat ini jika tidak diisi
                }

                // Validasi status berdasarkan jam masuk
                $expectedCheckInTime = Carbon::parse('08:00:00');
                $actualCheckInTime = Carbon::parse($attendance->check_in_time);
                $attendance->status = $actualCheckInTime->gt($expectedCheckInTime) ? 'late' : 'on time';

                // Validasi jam keluar
                if ($attendance->check_out_time) {
                    $checkOutTime = Carbon::parse($attendance->check_out_time);
                    if ($checkOutTime->lt($actualCheckInTime)) {
                        throw new \Exception("Check-out time cannot be earlier than check-in time.");
                    }
                }
            }
        });
    }

    /**
     * Accessor for attachment URL.
     */
    public function getAttachmentUrlAttribute()
    {
        return $this->attachment ? Storage::url($this->attachment) : null;
    }
}
