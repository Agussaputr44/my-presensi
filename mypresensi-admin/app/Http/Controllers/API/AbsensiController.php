<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\Attendance;
use App\Models\User;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;

class AbsensiController extends Controller
{



    public function store(Request $request)
    {
        try {
            $validated = $request->validate([
                'user_id' => 'required|exists:users,id',
                'location_id' => 'required|exists:locations,id',
                'location_name' => 'nullable|string',
                'type' => 'required|string',
                'latitude' => 'required|numeric',
                'longitude' => 'required|numeric',
                'check_in_time' => 'required_if:type,hadir|date_format:H:i:s',
                'check_out_time' => 'nullable|required_if:type,hadir|date_format:H:i:s',
                'reason' => 'nullable|string',
                'device_name' => 'required|string',
                'attachment' => 'nullable|file|mimes:jpg,jpeg,png,pdf,doc,docx|max:2048',
            ]);
    
            if ($validated['type'] == 'hadir') {
                $checkInTime = Carbon::parse($validated['check_in_time']);
                $expectedTime = Carbon::parse('08:00:00');
                $status = $checkInTime->gt($expectedTime) ? 'late' : 'on time';
            } else {
                $status = 'not applicable';
            }
    
            $attachmentPath = null;
            if ($request->hasFile('attachment')) {
                $attachmentPath = $request->file('attachment')->store('attachments', 'public');
            }
    
            $attendance = Attendance::create([
                'user_id' => $validated['user_id'],
                'location_id' => $validated['location_id'],
                'location_name' => $validated['location_name'] ?? null,
                'type' => $validated['type'],
                'status' => $status,
                'latitude' => $validated['latitude'],
                'longitude' => $validated['longitude'],
                'reason' => $validated['reason'] ?? null,
                'check_in_time' => $validated['check_in_time'] ?? null,
                'check_out_time' => $validated['check_out_time'] ?? null,
                'device_name' => $validated['device_name'],
                'attachment' => $attachmentPath,
            ]);
    
            return response()->json([
                'message' => 'Attendance recorded successfully',
                'data' => $attendance
            ], 201);
        } catch (\Illuminate\Validation\ValidationException $e) {
            // Handle validation errors
            return response()->json([
                'message' => 'Validation failed',
                'errors' => $e->errors()
            ], 422);
        } catch (\Exception $e) {
            // Log any other exceptions
            Log::error('Attendance creation failed', ['error' => $e->getMessage(), 'stack' => $e->getTraceAsString()]);
            return response()->json([
                'message' => 'An error occurred while recording attendance',
                'error' => $e->getMessage()
            ], 500);
        }
    }
    // public function index()
    // {
    //     $absensi = Attendance::all();

    //     return response()->json([
    //         'message' => 'Success',
    //         'data' => $absensi
    //     ], 200);
    // }

    // public function show(string $id)
    // {
    //     $attendance = Attendance::find($id);

    //     if (!$attendance) {
    //         return response()->json([
    //             'message' => 'Attendance record not found'
    //         ], 404);
    //     }

    //     return response()->json([
    //         'message' => 'Success',
    //         'data' => $attendance
    //     ], 200);
    // }


    // public function update(Request $request, string $id)
    // {
    //     // Find the attendance record by ID
    //     $attendance = Attendance::find($id);

    //     if (!$attendance) {
    //         return response()->json([
    //             'message' => 'Attendance record not found'
    //         ], 404);
    //     }

    //     $validated = $request->validate([
    //         'location_id' => 'nullable|exists:locations,id',
    //         'location_name' => 'nullable|string',
    //         'type' => 'nullable|string',
    //         'status' => 'nullable|string',
    //         'latitude' => 'nullable|numeric',
    //         'longitude' => 'nullable|numeric',
    //         'reason' => 'nullable|string',
    //         'check_in_time' => 'nullable|required_if:type,hadir|date_format:H:i:s', 
    //         'check_out_time' => 'nullable|required_if:type,hadir|date_format:H:i:s', 
    //     ]);

    //     if (isset($validated['check_in_time'])) {
    //         $checkInTime = Carbon::parse($validated['check_in_time']);
    //         $expectedTime = Carbon::parse('08:00:00');
    //         $status = $checkInTime->gt($expectedTime) ? 'late' : 'on time';
    //     } else {
    //         $status = $attendance->status; 
    //     }

    //     $attendance->update(array_merge($validated, ['status' => $status]));

    //     return response()->json([
    //         'message' => 'Attendance updated successfully',
    //         'data' => $attendance
    //     ], 200);
    // }


    // public function destroy(string $id)
    // {
    //     // Find the attendance record by ID
    //     $attendance = Attendance::find($id);

    //     if (!$attendance) {
    //         return response()->json([
    //             'message' => 'Attendance record not found'
    //         ], 404);
    //     }

    //     // Delete the attendance record
    //     $attendance->delete();

    //     return response()->json([
    //         'message' => 'Attendance deleted successfully'
    //     ], 200);
    // }
}
